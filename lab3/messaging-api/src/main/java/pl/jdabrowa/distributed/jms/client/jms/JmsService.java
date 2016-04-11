package pl.jdabrowa.distributed.jms.client.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.*;

public class JmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsService.class);

    private static final String QUEUE_NAME = "myQueue";

    private final Session session;
    private final MessageProducer producer;
    private final Destination returnQueue;

    public JmsService(String queueName, Connection connection, HandlerFactory handlerFactory) throws MessagingException {

        try {
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();

            this.returnQueue = session.createTemporaryQueue();
            Destination destination = this.session.createQueue(queueName);
            this.producer = session.createProducer(destination);

            registerReturnQueueHandler(handlerFactory);

            LOGGER.info("Initializing connection {} to queue {}", connection, destination);

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
