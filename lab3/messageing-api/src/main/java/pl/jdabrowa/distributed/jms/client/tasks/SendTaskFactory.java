package pl.jdabrowa.distributed.jms.client.tasks;

public interface SendTaskFactory {
    MessageSendTask createTask(byte [] sendMessagePayload);
}
