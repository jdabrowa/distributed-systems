package pl.jdabrowa.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.jms.server.Server;

public class EchoServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerMain.class);

    private void start(String queueName, String brokerLocator) throws Exception {
        new Broker(brokerLocator).start();
        Server server = new Server(queueName, new EchoServerHandler());
    }

    public static void main( String[] args ) throws Exception {
        LOGGER.info("Starting server");
        new EchoServerMain().start("echoQueue", "tcp://localhost:61616");
        LOGGER.info("Server started");
    }
}
