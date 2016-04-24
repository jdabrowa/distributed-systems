package pl.jdabrowa.distributed.jms.client.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;

@Component
public class SingleConnectionStrategy implements JmsServiceCreatingStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleConnectionStrategy.class);

    private final HandlerFactory handlerFactory;
    private volatile Connection cachedConnection = null;

    @Autowired
    public SingleConnectionStrategy(HandlerFactory factory) {
        this.handlerFactory = factory;
    }

    @Override
    public JmsService createService(ConnectionFactory factory, String queueName) throws MessagingException {
        createAndCacheConnectionIfNull(factory);
        return new JmsService(queueName, cachedConnection, handlerFactory);
    }

    private void createAndCacheConnectionIfNull(ConnectionFactory factory) throws MessagingException {
        if(null == cachedConnection) {
            synchronized (this) {
                if(null == cachedConnection) {
                    tryCreateAndCacheConnection(factory);
                }
            }
        }
    }

    private void tryCreateAndCacheConnection(ConnectionFactory factory) throws MessagingException {
        LOGGER.info("Creating JMS connection");
        try {
            cachedConnection = factory.createConnection();
        } catch (JMSException e) {
            throw new MessagingException("Failed to create connection", e);
        }
        LOGGER.info("Connection created: {}", cachedConnection);
    }
}
