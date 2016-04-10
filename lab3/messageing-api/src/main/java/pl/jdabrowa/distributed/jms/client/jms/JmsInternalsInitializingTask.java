package pl.jdabrowa.distributed.jms.client.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JmsInternalsInitializingTask implements Runnable {

    private final ThreadLocalJmsProvider provider;

    @Autowired
    public JmsInternalsInitializingTask(ThreadLocalJmsProvider provider) {
        this.provider = provider;
    }

    @Override
    public void run() {

    }
}
