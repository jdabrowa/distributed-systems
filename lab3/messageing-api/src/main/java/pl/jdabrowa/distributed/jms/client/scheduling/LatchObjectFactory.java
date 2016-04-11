package pl.jdabrowa.distributed.jms.client.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class LatchObjectFactory implements WaitObjectFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LatchObjectFactory.class);

    @Override
    public WaitObject createWaitObject(long timeoutMillis) {
        return new LatchWaitObject(timeoutMillis);
    }

    private static final class LatchWaitObject implements WaitObject {

        private final CountDownLatch latch;
        private final long timeoutMillis;

        private LatchWaitObject(long timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void await() throws TimeoutException {
            try {
                awaitWithTimeout();
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted while waiting for latch", e);
            }
        }

        private void awaitWithTimeout() throws InterruptedException, TimeoutException {
            boolean waitResult = latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
            if(!waitResult) {
                throw new TimeoutException("Timeout occurred while waiting for result");
            }
        }

        @Override
        public void release() {
            latch.countDown();
        }
    }
}
