package pl.jdabrowa.distributed.jms.client.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.scheduling.TaskCoordinator;

import javax.jms.MessageListener;

@Component
public class HandlerFactory {

    private final TaskCoordinator coordinator;

    @Autowired
    public HandlerFactory(TaskCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public MessageListener createListener() {
        return new PerThreadHandler(coordinator);
    }
}
