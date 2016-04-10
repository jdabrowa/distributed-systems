package pl.jdabrowa.distributed.jms.client.tasks;

public interface SendTask extends Runnable{
    String getUniqueId();
}
