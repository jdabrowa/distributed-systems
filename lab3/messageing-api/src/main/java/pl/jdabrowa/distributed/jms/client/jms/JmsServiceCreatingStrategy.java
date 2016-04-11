package pl.jdabrowa.distributed.jms.client.jms;

import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

public interface JmsServiceCreatingStrategy {
    JmsService createService(ConnectionFactory factory, Destination destination) throws MessagingException;
}
