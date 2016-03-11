package com.jdabrowa.distributed.zad1.protocol;

import java.io.IOException;
import java.net.Socket;

public class SocketWriter implements MessageWriter {

    private final Socket socket;

    public SocketWriter(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void sendMessage(OutgoingMessage message) throws IOException {
        socket.getOutputStream().write(message.getBytes());
    }
}
