package pl.jdabrowa.distributed.jms.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.Service;

@Component
public class ThreadPoolService implements Service{

    private final SendTaskFactory taskFactory;

    @Autowired
    public ThreadPoolService(SendTaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    @Override
    public byte[] sendAndReceive(byte[] requestPayload) {

        Runnable sendingTask = taskFactory.createTask(requestPayload);


        return new byte[0];
    }
}
