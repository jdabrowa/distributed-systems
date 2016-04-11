package pl.jdabrowa.distributed.jms.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.scheduling.TaskCoordinator;
import pl.jdabrowa.distributed.jms.client.scheduling.ThreadPoolService;
import pl.jdabrowa.distributed.jms.client.tasks.SendTaskFactory;

@Component
public class ThreadPoolServiceFactory implements ServiceFactory {

    private final SendTaskFactory taskFactory;
    private final TaskCoordinator coordinator;

    @Autowired
    public ThreadPoolServiceFactory(SendTaskFactory taskFactory, TaskCoordinator coordinator) {
        this.taskFactory = taskFactory;
        this.coordinator = coordinator;
    }

    @Override
    public Service createService(ServiceConfiguration configuration) {
        return new ThreadPoolService(taskFactory, coordinator);
    }
}
