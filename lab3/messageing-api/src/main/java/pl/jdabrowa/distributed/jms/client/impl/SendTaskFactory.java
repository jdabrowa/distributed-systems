package pl.jdabrowa.distributed.jms.client.impl;

public interface SendTaskFactory {
    MessageSendTask createTask(byte [] sendMessagePayload);
}
