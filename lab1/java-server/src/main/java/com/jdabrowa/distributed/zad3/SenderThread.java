package com.jdabrowa.distributed.zad3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class SenderThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderThread.class);

    private final ChatConnector connector;
    private final String nick;
    private final Scanner scanner;

    public SenderThread(String nick, ChatConnector connector) {
        super("SenderThread");
        this.connector = connector;
        this.nick = nick;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        LOGGER.info("Entering sender loop...");
        boolean shouldContinue = true;
        while(shouldContinue) {
            String line = scanner.nextLine();
            // TODO: Paginate that
            if(line.length() < 20) {
                ChatMessage message = new ChatMessage(new Date(), nick, line);
                try {
                    connector.sendMessage(message);
                } catch (IOException e) {
                    LOGGER.info("Error on message send", e);
                }
            }
        }
        LOGGER.info("Exiting sender loop...");
    }
}
