package pl.jdabrowa.distributed.jms.client.scheduling;

public interface TaskExecutor {
    public void execute(Runnable task);
}
