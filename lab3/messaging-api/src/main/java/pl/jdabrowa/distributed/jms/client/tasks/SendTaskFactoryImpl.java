package pl.jdabrowa.distributed.jms.client.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;
import pl.jdabrowa.distributed.jms.client.jms.ThreadLocalJmsProvider;

@Component
public class SendTaskFactoryImpl implements SendTaskFactory {

    private final ThreadLocalJmsProvider jmsProvider;
    private final IdGenerationStrategy idGenerationStrategy;

    @Autowired
    public SendTaskFactoryImpl(ThreadLocalJmsProvider jmsProvider, IdGenerationStrategy idGenerationStrategy) {
        this.jmsProvider = jmsProvider;
        this.idGenerationStrategy = idGenerationStrategy;
    }

    @Override
    public MessageSendTask createTask(byte [] taskBytes) throws MessagingException {
        String uniqueId = idGenerationStrategy.generateUniqueId();
        return new MessageSendTask(uniqueId, taskBytes, jmsProvider);
    }
}
