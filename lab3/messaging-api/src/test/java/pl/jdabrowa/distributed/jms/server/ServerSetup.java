package pl.jdabrowa.distributed.jms.server;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSetup.class);

    @Bean
    public BrokerService createBroker() throws Exception {
        LOGGER.info("Creating broker...");
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector("tcp://localhost:61616");
        broker.start();
        return broker;
    }
}
