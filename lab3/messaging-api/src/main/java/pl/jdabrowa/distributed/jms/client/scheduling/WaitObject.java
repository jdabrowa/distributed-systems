package pl.jdabrowa.distributed.jms.client.scheduling;

import java.util.concurrent.TimeoutException;

public interface WaitObject {
    void await() throws TimeoutException;
    void release();
}
