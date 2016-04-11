package pl.jdabrowa.distributed.jms.client.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class PoolExecutor implements TaskExecutor {

    private final Executor executor;

    @Autowired
    public PoolExecutor(@Value("${messaging.pool.size}") int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
