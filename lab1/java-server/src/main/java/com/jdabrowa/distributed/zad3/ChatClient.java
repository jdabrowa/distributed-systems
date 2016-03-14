package com.jdabrowa.distributed.zad3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChatClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);

    private final ChatConnector connector;

    public ChatClient(ChatConnector connector, String nick) throws InterruptedException {
        this.connector = connector;
        ReceiverThread receiverThread = new ReceiverThread(connector);
        SenderThread senderThread = new SenderThread(nick, connector);
        LOGGER.info("Starting chat threads...");
        receiverThread.start();
        senderThread.start();
        LOGGER.info("Chat threads started");
        senderThread.join();
        receiverThread.join();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 1 || args[0].length() > 6) {
            LOGGER.warn("One program argument required - user nick name (max. 6 characters)");
            throw new IllegalArgumentException("One program argument required - user nick name (max. 6 characters)");
        }
        String nick = args[0];
        InetAddress groupAddress = InetAddress.getByName("228.5.6.2");
        int portNumber = 26777;
        MulticastSocket socket = new MulticastSocket(portNumber);
        MessageReader reader = new MessageReader(socket);
        ChatConnector chatConnector = new ChatConnector(groupAddress, portNumber, socket, reader);
        ChatClient client = new ChatClient(chatConnector, nick);
    }
}
