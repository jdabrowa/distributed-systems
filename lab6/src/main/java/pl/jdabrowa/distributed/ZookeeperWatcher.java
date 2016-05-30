package pl.jdabrowa.distributed;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZookeeperWatcher implements Watcher, AsyncCallback.StatCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperWatcher.class);

    private final String executableName;
    private final ZooKeeper zooKeeper;
    private final String nodeName;

    private boolean previousStatus = false;
    private Process child;

    private Set<String> children = new HashSet<>();

    private int numChildren = 0;
    private int lastLogged = -1;

    public ZookeeperWatcher(ZooKeeper zooKeeper, String executableName, String nodeName) throws KeeperException, InterruptedException {
        this.executableName = executableName;
        this.zooKeeper = zooKeeper;
        this.nodeName = nodeName;
        zooKeeper.exists(this.nodeName, true, this, null);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        String path = watchedEvent.getPath();
//        LOGGER.info("Processing event for node: {}", path);
//        LOGGER.info("{}", watchedEvent.toString());
        if(nodeName.equals(path)) {
            zooKeeper.exists(nodeName, true, this, null);
        }
        if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            visitChildren(path);
        }
    }

    private void visitChildren(String path) {
        try {
            List<String> children = zooKeeper.getChildren(path, true);
            for (String s : children) {
//                LOGGER.info("{} is child of {}", s, path);
                zooKeeper.exists(path + "/" + s, true, this, null);
                zooKeeper.getChildren(path + "/" + s, true);
            }
        } catch (KeeperException | InterruptedException e) {
            LOGGER.warn("Error", e);
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean nodeExists = false;
        if(isOK(rc)) {
            nodeExists = true;
        } else if(nodeDoesNotExist(rc)){
            nodeExists = false;
        }

//        LOGGER.info("Processing result for: {}. Exists: {}", path, nodeExists);

        if(nodeExists) {
            visitChildren(path);
        }

        if (path.equals(nodeName) && statusChanged(path, nodeExists)) {
            if(nodeExists) {
                startExecutable();
            } else {
                stopExecutable();
            }
        }

        if(statusChanged(path, nodeExists)) {
            if(nodeExists) {
                ++numChildren;
            } else {
                --numChildren;
            }
        }

        if(lastLogged != numChildren) {
            LOGGER.info("Number of nodes: {}", numChildren);
            lastLogged = numChildren;
        }

        if(nodeExists) {
            children.add(path);
        } else {
            children.remove(path);
        }
    }

    private void stopExecutable() {
        child.destroy();
        try {
            child.waitFor();
        } catch (InterruptedException e) {
            LOGGER.warn("Error while terminating child process: {}", executableName, e);
        }
    }

    private void startExecutable() {
        try {
            this.child = Runtime.getRuntime().exec(executableName);
        } catch (IOException e) {
            LOGGER.warn("Cannot start executable {}", executableName, e);
        }
    }

    private boolean statusChanged(String path, boolean nodeExists) {
        boolean status = children.contains(path);
        return nodeExists != status;
    }

    private boolean nodeDoesNotExist(int rc) {
        return KeeperException.Code.NONODE.intValue() == rc;
    }

    private boolean isOK(int rc) {
        return KeeperException.Code.OK.intValue() == rc;
    }
}
