package pl.jdabrowa.distributed.jms.client.impl;

import org.springframework.beans.factory.annotation.Autowired;

public class MessageSendTask implements Runnable {

    private final ThreadLocalJmsProvider jmsProvider;

    @Autowired
    public MessageSendTask(ThreadLocalJmsProvider jmsProvider) {
        this.jmsProvider = jmsProvider;
    }

    @Override
    public void run() {

    }
}
