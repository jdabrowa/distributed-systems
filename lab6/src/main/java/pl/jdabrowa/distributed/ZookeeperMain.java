package pl.jdabrowa.distributed;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperMain implements Watcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperMain.class);

    private static final String EXECUTABLE_NAME = "gnome-calculator";
    private static final String connectionString = "127.0.0.1:3000";
    private static final String OBSERVED_ZNODE_NAME = "/znode_testowy";

    private final ZooKeeper zooKeeper;
    private final ZookeeperWatcher watcher;

    public ZookeeperMain() throws IOException, KeeperException, InterruptedException {
        this.zooKeeper = new ZooKeeper(connectionString, 3000, this);
        this.watcher = new ZookeeperWatcher(zooKeeper, EXECUTABLE_NAME, OBSERVED_ZNODE_NAME);
    }

    @Override
    public void process(WatchedEvent event) {
        watcher.process(event);
    }

    public void start() {
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.warn("Error", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ZookeeperMain().start();
    }
}
