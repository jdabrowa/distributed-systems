package pl.jdabrowa.distributed.jms.client.tasks;

import pl.jdabrowa.distributed.jms.client.error.MessagingException;

public interface SendTaskFactory {
    MessageSendTask createTask(byte [] sendMessagePayload) throws MessagingException;
}
