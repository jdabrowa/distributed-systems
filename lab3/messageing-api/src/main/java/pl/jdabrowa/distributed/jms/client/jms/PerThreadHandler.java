package pl.jdabrowa.distributed.jms.client.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;
import pl.jdabrowa.distributed.jms.client.scheduling.TaskCoordinator;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class PerThreadHandler implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerThreadHandler.class);

    private final TaskCoordinator coordinator;

    @Autowired
    public PerThreadHandler(TaskCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String requestId = message.getJMSCorrelationID();
            BytesMessage castedBytesMessage = (BytesMessage) message;
            byte[] messageBytes = readBytes(castedBytesMessage);
            coordinator.publishResultFor(requestId, messageBytes);
        } catch (Exception e) {
            LOGGER.warn("Error during response message processing", e);
            // TODO: Notify waiting thread about failure, probably return some kind of success/failure structure or optional instead of result
        }
    }

    private byte[] readBytes(BytesMessage message) throws JMSException, MessagingException {
        int bodyLength = (int) message.getBodyLength();
        byte [] messageBytes = new byte[bodyLength];
        int bytesRead = message.readBytes(messageBytes);
        assertAllRead(bodyLength, bytesRead);
        return messageBytes;
    }

    private void assertAllRead(int bodyLength, int bytesRead) throws MessagingException {
        if(bytesRead != bodyLength) {
            throw new MessagingException("Failed to read incoming message content");
        }
    }
}
