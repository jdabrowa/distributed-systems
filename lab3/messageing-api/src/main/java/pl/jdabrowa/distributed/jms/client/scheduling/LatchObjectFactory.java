package pl.jdabrowa.distributed.jms.client.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class LatchObjectFactory implements WaitObjectFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LatchObjectFactory.class);

    @Override
    public WaitObject createWaitObject() {
        return null;
    }

    private static final class LatchWaitObject implements WaitObject {

        private final CountDownLatch latch;

        private LatchWaitObject() {
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void await() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted while waiting for latch", e);
            }
        }

        @Override
        public void release() {
            latch.countDown();
        }
    }
}
