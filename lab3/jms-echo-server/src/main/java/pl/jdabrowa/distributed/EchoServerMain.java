package pl.jdabrowa.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.jms.server.Server;

public class EchoServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerMain.class);

    private void start(String echoQueueName, String twiceQueueName, String brokerLocator) throws Exception {
        new Broker(brokerLocator).start();
        Server echoServer = new Server(echoQueueName, new EchoServerHandler());
        Server doubleServer = new Server(twiceQueueName, new DoublingHandler());
    }

    public static void main( String[] args ) throws Exception {

        if(args.length != 2) {
            String errorMessage = "Requires two arguments: queue names for echo queue and doubling queue";
            LOGGER.warn(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        LOGGER.info("Starting server");
        new EchoServerMain().start(args[0], args[1], "tcp://localhost:61616");
        LOGGER.info("Server started");
    }
}
