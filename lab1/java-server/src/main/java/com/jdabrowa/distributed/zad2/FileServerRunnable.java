package com.jdabrowa.distributed.zad2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileServerRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServerRunnable.class);

    private final Socket socket;
    private final ImageLoader loader;
    private final SocketWriter writer;

    public FileServerRunnable(Socket socket, ImageLoader loader, SocketWriter writer) {
        this.socket = socket;
        this.loader = loader;
        this.writer = writer;
    }

    @Override
    public void run() {
        LOGGER.debug("Request processing start");
        long startTime = System.currentTimeMillis();
        try {
            processRequestAndCloseSocket();
        } catch (IOException e) {
            LOGGER.warn("IOException during file request handling", e);
        } finally {
            long requestDuration = System.currentTimeMillis() - startTime;
            LOGGER.debug("Request processing finished in {} ms", requestDuration);
        }
    }

    private void processRequestAndCloseSocket() throws IOException {
        try {
            processRequest();
        } finally {
            LOGGER.debug("Closing request socket...");
            socket.close();
            LOGGER.debug("Request socket closed");
        }
    }

    private void processRequest() throws IOException {
        String imageName = retrieveImageNameFromRequest(socket);
        LOGGER.debug("Look-up for image {}", imageName);
        if(loader.isMappingAvailableFor(imageName)) {
            sendImageInSuccessfulRequest(imageName);
        } else {
            LOGGER.debug("Request failed - no mapping for {}", imageName);
            sendRequestFailedStatus();
        }
    }

    private void sendRequestFailedStatus() throws IOException {
        writer.sendInt(StatusConstants.NO_SUCH_IMAGE);
    }

    private void sendImageInSuccessfulRequest(String imageSymbolicName) throws IOException {
        InputStream imageInputStream = loader.loadImageAsStream(imageSymbolicName);
        String imageFileName = loader.getFileNameForImage(imageSymbolicName);
        LOGGER.debug("Received request for {}. Mapped image is: {}", imageSymbolicName, imageFileName);
        writer.sendInt(StatusConstants.REQUEST_SUCCESSFUL);
        writer.sendStringWithLength(imageFileName);
        writer.sendInt(imageInputStream.available());
        writer.sendStreamContent(imageInputStream);
        LOGGER.debug("Response with {} image sent", imageSymbolicName);
    }

    private String retrieveImageNameFromRequest(Socket socket) throws IOException {
        SocketReader reader = new SocketReader(socket);
        FileRequest request = reader.readRequest();
        return request.getFileName();
    }
}
