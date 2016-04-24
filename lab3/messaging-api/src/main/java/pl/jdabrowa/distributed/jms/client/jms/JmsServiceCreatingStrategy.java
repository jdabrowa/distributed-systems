package pl.jdabrowa.distributed.jms.client.jms;

import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.ConnectionFactory;

public interface JmsServiceCreatingStrategy {
    JmsService createService(ConnectionFactory factory, String queueName) throws MessagingException;
}
