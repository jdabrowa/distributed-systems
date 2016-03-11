package com.jdabrowa.distributed.zad1.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketReader implements MessageReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketReader.class);

    private final DataInputStream inputStream;

    public SocketReader(Socket socket) throws IOException {
        this.inputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public Message readMessage() throws IOException{

        LOGGER.debug("Reading data from stream. Available bytes: {}", inputStream.available());

        byte oneByte = inputStream.readByte();
        short twoBytes = inputStream.readShort();
        int fourBytes = inputStream.readInt();
        long eightBytes = inputStream.readLong();

        LOGGER.debug("Received message: 1-byte {}, 2-bytes {}, 4-bytes {}, 8-bytes {}", oneByte, twoBytes, fourBytes, eightBytes);

        return new Message(oneByte, twoBytes, fourBytes, eightBytes);
    }
}
