package pl.jdabrowa.distributed.jms.client.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.Service;
import pl.jdabrowa.distributed.jms.client.tasks.MessageSendTask;
import pl.jdabrowa.distributed.jms.client.tasks.SendTaskFactory;

@Component
public class ThreadPoolService implements Service{

    private final SendTaskFactory taskFactory;
    private final TaskCoordinator messageTaskTaskCoordinator;

    @Autowired
    public ThreadPoolService(SendTaskFactory taskFactory, TaskCoordinator taskCoordinator) {
        this.taskFactory = taskFactory;
        this.messageTaskTaskCoordinator = taskCoordinator;
    }

    @Override
    public byte[] sendAndReceive(byte[] requestPayload) {

        MessageSendTask sendingTask = taskFactory.createTask(requestPayload);
        messageTaskTaskCoordinator.awaitCompletion(sendingTask);
        return messageTaskTaskCoordinator.retrieveResultOf(sendingTask.getUniqueId());
    }
}
