package com.jdabrowa.distributed.zad2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketWriter.class);

    private static final int DEFAULT_CHUNK_SIZE = 1024;
    public static final int END_OF_DATA_MARKER = -1;

    private final Socket socket;

    public SocketWriter(Socket socket) {
        this.socket = socket;
    }

    public void sendStringWithLength(String string) throws IOException {
        LOGGER.debug("Sending String in request: '{}'", string);
        DataOutputStream socketOutputStream = new DataOutputStream(socket.getOutputStream());
        int stringLength = string.length();
        socketOutputStream.writeInt(stringLength);
        for(char c : string.toCharArray()) {
            socketOutputStream.writeByte((byte) c);
        }
        LOGGER.debug("String sent");
    }

    public void sendStreamContent(InputStream toSend) throws IOException {
        byte [] chunkBuffer = new byte[DEFAULT_CHUNK_SIZE];
        int bytesRead;
        OutputStream socketOutputStream = socket.getOutputStream();
        LOGGER.debug("Sending {} bytes from stream to socket...", toSend.available());
        while((bytesRead = toSend.read(chunkBuffer)) != END_OF_DATA_MARKER) {
            socketOutputStream.write(chunkBuffer, 0, bytesRead);
        }
        LOGGER.debug("Stream request sent");
    }

    public void sendInt(int anInt) throws IOException {
        LOGGER.debug("Sending integer to socket: {}", anInt);
        DataOutputStream socketOutputStream = new DataOutputStream(socket.getOutputStream());
        socketOutputStream.writeInt(anInt);
        LOGGER.debug("Integer sent");
    }
}
