package pl.jdabrowa.distributed.jms.client.scheduling;

public interface WaitObject {
    void await();
    void release();
}
