package pl.jdabrowa.distributed.jms.client.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

@Component
public class JmsInternalsInitializingTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsInternalsInitializingTask.class);

    private final ThreadLocalJmsProvider provider;

    @Autowired
    public JmsInternalsInitializingTask(ThreadLocalJmsProvider provider) {
        this.provider = provider;
    }

    @Override
    public void run() {
        try {
            JmsService jmsService = provider.get();
        } catch (MessagingException e) {
            LOGGER.warn("Failed to initialize JMS components", e);
        }
    }
}
