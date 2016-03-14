package com.jdabrowa.distributed.zad3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ReceiverThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverThread.class);

    private final ChatConnector connector;

    public ReceiverThread(ChatConnector connector) {
        super("ReceiverThread");
        this.connector = connector;
    }

    @Override
    public void run() {
        LOGGER.info("Entering receiver loop...");
        boolean shouldContinue = true;
        while(shouldContinue) {
            try {
                ChatMessage chatMessage = connector.receiveMessage();
                printMessage(chatMessage);
            } catch (CorruptedMessageException | IOException e) {
                LOGGER.info("Error during message receiving", e);
            }
        }
        LOGGER.info("Exiting receiver loop...");
    }

    private void printMessage(ChatMessage chatMessage) {
        String chatLineToPrint = String.format("%s (%s) > %s", chatMessage.getNickName(), chatMessage.getDate(), chatMessage.getMessage());
        System.out.println(chatLineToPrint);
    }
}
