package pl.jdabrowa.distributed;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Broker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Broker.class);
    private final String locator;

    public Broker(String locator) {
        this.locator = locator;
    }

    public void start() throws Exception {
        LOGGER.info("Creating broker...");
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector(locator);
        broker.start();
        LOGGER.info("Broker started");
    }
}
