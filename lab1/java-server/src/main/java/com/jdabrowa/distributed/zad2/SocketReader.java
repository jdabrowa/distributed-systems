package com.jdabrowa.distributed.zad2;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketReader {

    private final Socket socket;

    public SocketReader(Socket socket) {
        this.socket = socket;
    }

    public FileRequest readRequest() throws IOException {
        DataInputStream socketDataStream = inputDataStreamForSocket(socket);
        int requestLength = socketDataStream.readInt();
        String requestedFileName = readStringAsAsciiSequenceOfLength(socketDataStream, requestLength);
        return new FileRequest(requestedFileName);
    }

    private String readStringAsAsciiSequenceOfLength(DataInputStream socketDataStream, int requestLength) throws IOException {
        StringBuilder builder = new StringBuilder(requestLength);
        for (int i = 0; i < requestLength; i++) {
            builder.append((char) socketDataStream.readByte());
        }
        return builder.toString();
    }

    private DataInputStream inputDataStreamForSocket(Socket socket) throws IOException {
        InputStream socketInputStream = socket.getInputStream();
        return new DataInputStream(socketInputStream);
    }
}
