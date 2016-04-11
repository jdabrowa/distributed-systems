package pl.jdabrowa.distributed.jms.client.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;

@Component
public class SingleConnectionStrategy implements JmsServiceCreatingStrategy {

    private final HandlerFactory handlerFactory;
    private volatile Connection cachedConnection = null;

    @Autowired
    public SingleConnectionStrategy(HandlerFactory factory) {
        this.handlerFactory = factory;
    }

    @Override
    public JmsService createService(ConnectionFactory factory) throws MessagingException {
        createAndCacheConnectionIfNull(factory);
        return new JmsService(cachedConnection, handlerFactory);
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
        try {
            cachedConnection = factory.createConnection();
        } catch (JMSException e) {
            throw new MessagingException("Failed to create connection", e);
        }
    }
}
