package pl.jdabrowa.distributed.jms.client.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;
import pl.jdabrowa.distributed.jms.client.jms.JmsService;
import pl.jdabrowa.distributed.jms.client.jms.ThreadLocalJmsProvider;

import javax.jms.JMSException;

public class MessageSendTask implements SendTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendTask.class);

    private final String uniqueId;
    private final JmsService jmsService;
    private final byte[] requestBytes;

    public MessageSendTask(String uniqueId, byte [] requestBytes, ThreadLocalJmsProvider jmsProvider) throws MessagingException {
        this.uniqueId = uniqueId;
        this.jmsService = jmsProvider.get();
        this.requestBytes = requestBytes;
    }

    @Override
    public void run() {
        try {
            jmsService.sendMessage(requestBytes, uniqueId);
        } catch (JMSException e) {
            LOGGER.warn("Failed to send message", e);
        }
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
}