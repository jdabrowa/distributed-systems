package com.jdabrowa.distributed.zad1;

import ch.qos.logback.classic.BasicConfigurator;
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

    private void start() throws IOException {

        Rfc3091Server server = createServer();
        initializeServer(server);
        server.start();
    }

    private void initializeServer(Rfc3091Server server) throws IOException {
        server.bind22by7DigitGeneration(22333);
        server.bindPiDigitGeneration(22444);
    }

    private Rfc3091Server createServer() {
        Executor threadPool = Executors.newFixedThreadPool(2);
        Rfc3091Service service = new SimpleService(new MockCalculator());
        return new ThreadedSocketServer(threadPool, service);
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting server...");
        new ServerMain().start();
        LOGGER.info("Server started!");
    }
}
