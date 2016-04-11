package pl.jdabrowa.distributed.jms.server;

import lombok.Getter;
import org.apache.activemq.ActiveMQConnectionFactory;
import pl.jdabrowa.distributed.jms.server.error.ServerException;

import javax.jms.*;

public class JmsInternals {

    private static final String DEFAULT_BROKER = "tcp://localhost:61616";

    @Getter private final MessageConsumer consumer;
    @Getter private final MessageProducer producer;
    private final Session session;

    public JmsInternals(String queueName) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination incomingQueue = session.createQueue(queueName);
        this.consumer = session.createConsumer(incomingQueue);
        this.producer = session.createProducer(null);
        this.session = session;
    }

    public BytesMessage createMessage() throws JMSException {
        return session.createBytesMessage();
    }
}
