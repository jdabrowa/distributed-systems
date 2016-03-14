package com.jdabrowa.distributed.zad2;

import com.jdabrowa.distributed.zad1.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServer.class);

    private static final String FILE_SERVER_PORT_PROPERTY_KEY = "com.jdabrowa.distributed.fileServerPort";

    private final PropertiesLoader propertiesLoader;

    public FileServer(PropertiesLoader propertiesLoader) throws IOException {
        this.propertiesLoader = propertiesLoader;
        this.propertiesLoader.loadProperties("configuration.properties");
    }

    public void start() throws IOException {
        LOGGER.info("Starting FileServer...");
        int fileServerPortNumber =
                Integer.valueOf(propertiesLoader.getProperty(FILE_SERVER_PORT_PROPERTY_KEY));
        LOGGER.info("Initializing file service on port {}", fileServerPortNumber);
        ServerSocket serverSocket = new ServerSocket(fileServerPortNumber, 25);
        PropertiesLoader imagePropertiesLoader = new PropertiesLoader();
        ImageLoader loader = new ImageLoader(imagePropertiesLoader);
        LOGGER.info("FileServer initialized, awaiting requests");
        while(true) {
            Socket incommingConnectionSocket = serverSocket.accept();
            int localPort = incommingConnectionSocket.getLocalPort();
            LOGGER.debug("Request received, starting processing on port {}", localPort);
            SocketWriter writer = new SocketWriter(incommingConnectionSocket);
            FileServerRunnable runnable = new FileServerRunnable(incommingConnectionSocket, loader, writer);
            new Thread(runnable, "FileServerWorkerThread").start();
        }
    }

    public static void main(String[] args) throws IOException {
        new FileServer(new PropertiesLoader()).start();
    }
}
