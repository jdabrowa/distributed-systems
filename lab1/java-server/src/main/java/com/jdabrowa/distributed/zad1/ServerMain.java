package com.jdabrowa.distributed.zad1;

import com.jdabrowa.distributed.zad1.math.MockCalculator;
import com.jdabrowa.distributed.zad1.server.Rfc3091Server;
import com.jdabrowa.distributed.zad1.server.ThreadedSocketServer;
import com.jdabrowa.distributed.zad1.service.Rfc3091Service;
import com.jdabrowa.distributed.zad1.service.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    private static final String PI_PORT_PROP_KEY = "com.jdabrowa.distributed.piPort";
    private static final String THE_22_BY_7_PORT_PROP_KEY = "com.jdabrowa.distributed.approx22by7Port";
    public static final String CONFIGURATION_PROPERTIES = "configuration.properties";
    private final PropertiesLoader propertiesLoader;

    public ServerMain(PropertiesLoader propertiesLoader) throws IOException {
        this.propertiesLoader = propertiesLoader;
        this.propertiesLoader.loadProperties(CONFIGURATION_PROPERTIES);
    }

    private void start() throws IOException {

        Rfc3091Server server = createServer();
        initializeServer(server);
        server.start();
    }

    private void initializeServer(Rfc3091Server server) throws IOException {
        int _22By7PortNumber = Integer.valueOf(propertiesLoader.getProperty(THE_22_BY_7_PORT_PROP_KEY));
        int piPortNumber = Integer.valueOf(propertiesLoader.getProperty(PI_PORT_PROP_KEY));
        server.bind22by7DigitGeneration(_22By7PortNumber);
        server.bindPiDigitGeneration(piPortNumber);
    }

    private Rfc3091Server createServer() {
        Executor threadPool = Executors.newFixedThreadPool(2);
        Rfc3091Service service = new SimpleService(new MockCalculator());
        return new ThreadedSocketServer(threadPool, service);
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting server...");
        new ServerMain(new PropertiesLoader()).start();
        LOGGER.info("Server started!");
    }
}
