package pl.jdabrowa.distributed.jms.client.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.*;

@Component
public class JmsService {

    private final Session session;
    private final MessageProducer producer;
    private final Destination returnQueue;

    @Autowired
    public JmsService(Connection connection, Destination destination, HandlerFactory handlerFactory) throws MessagingException {

        try {
            this.session = connection.createSession();
            this.returnQueue = session.createTemporaryQueue();
            this.producer = session.createProducer(destination);

            registerReturnQueueHandler(handlerFactory);

            connection.start();
        } catch (JMSException e) {
            throw new MessagingException("Error on creating session", e);
        }

    }

    private void registerReturnQueueHandler(HandlerFactory handlerFactory) throws JMSException {
        MessageListener listener = handlerFactory.createListener();
        MessageConsumer consumer = session.createConsumer(returnQueue);
        consumer.setMessageListener(listener);
    }

    public void sendMessage(byte [] payload, String messageId) throws JMSException {
        BytesMessage message = session.createBytesMessage();
        message.writeBytes(payload);
        message.setJMSCorrelationID(messageId);
        message.setJMSReplyTo(returnQueue);
        producer.send(message);
    }
}
