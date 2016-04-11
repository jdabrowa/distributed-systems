package pl.jdabrowa.distributed.jms.client.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.tasks.MessageSendTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskCoordinator {

    private final Map<String, WaitObject> waitObjects;
    private final Map<String, byte []> results;

    private final WaitObjectFactory waitObjectFactory;
    private final TaskExecutor executor;

    @Autowired
    public TaskCoordinator(WaitObjectFactory waitObjectFactory, TaskExecutor executor) {

        this.waitObjects = new ConcurrentHashMap<>();
        this.results = new ConcurrentHashMap<>();

        this.waitObjectFactory = waitObjectFactory;
        this.executor = executor;
    }

    public void awaitCompletion(MessageSendTask task) {
        WaitObject waitObject = waitObjectFactory.createWaitObject();
        waitObjects.put(task.getUniqueId(), waitObject);
        executor.execute(task);
        waitObject.await();
    }

    public byte[] retrieveResultOf(String uniqueTaskId) {
        return results.remove(uniqueTaskId);
    }

    public void publishResultFor(String uniqueId, byte [] result) {
        results.put(uniqueId, result);
        WaitObject waitObjectForId = waitObjects.remove(uniqueId);
        waitObjectForId.release();
    }
}
