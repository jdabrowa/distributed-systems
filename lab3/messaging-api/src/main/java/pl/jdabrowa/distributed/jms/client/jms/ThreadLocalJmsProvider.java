package pl.jdabrowa.distributed.jms.client.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Component
public class ThreadLocalJmsProvider {

    private final ThreadLocal<JmsService> threadLocalService;

    @Value("${jms.configuration.jmsProperties.outQueueName}")
    private String queueName;

    @Autowired
    public ThreadLocalJmsProvider(ConnectionFactory connectionFactory, JmsServiceCreatingStrategy jmsServiceStrategy) {
        threadLocalService = createThreadLocal(jmsServiceStrategy, connectionFactory);
    }

    public JmsService get() throws MessagingException {
        try {
            return threadLocalService.get();
        } catch (Exception e) {
            throw new MessagingException("Could no initialize service", e);
        }
    }

    private ThreadLocal<JmsService> createThreadLocal(JmsServiceCreatingStrategy strategy, ConnectionFactory factory) {
        return new ThreadLocal<JmsService>() {
            @Override
            public JmsService initialValue() {
                try {
                    return strategy.createService(factory, queueName);
                } catch (MessagingException e) {
                    throw new RuntimeException("Failed to create service", e);
                }
            }
        };
    }

}
