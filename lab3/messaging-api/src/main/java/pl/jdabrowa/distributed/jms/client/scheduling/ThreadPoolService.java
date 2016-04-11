package pl.jdabrowa.distributed.jms.client.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.Service;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;
import pl.jdabrowa.distributed.jms.client.tasks.MessageSendTask;
import pl.jdabrowa.distributed.jms.client.tasks.SendTaskFactory;

import java.util.concurrent.TimeoutException;

@Component
public class ThreadPoolService implements Service{

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolService.class);

    private final SendTaskFactory taskFactory;
    private final TaskCoordinator messageTaskTaskCoordinator;

    @Autowired
    public ThreadPoolService(SendTaskFactory taskFactory, TaskCoordinator taskCoordinator) {
        LOGGER.info("Initializing thread pool service");
        this.taskFactory = taskFactory;
        this.messageTaskTaskCoordinator = taskCoordinator;
    }

    @Override
    public byte[] sendAndReceive(byte[] requestPayload, long timeoutMillis) throws MessagingException {
        MessageSendTask sendingTask = taskFactory.createTask(requestPayload);
        awaitWithTimeoutHandling(timeoutMillis, sendingTask);
        return messageTaskTaskCoordinator.retrieveResultOf(sendingTask.getUniqueId());
    }

    private void awaitWithTimeoutHandling(long timeoutMillis, MessageSendTask sendingTask) throws MessagingException {
        try {
            messageTaskTaskCoordinator.awaitCompletion(sendingTask, timeoutMillis);
        } catch (TimeoutException e) {
            throw new MessagingException("Request timed out", e);
        }
    }
}
