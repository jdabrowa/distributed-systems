package com.jdabrowa.distributed.zad1.server;

import com.jdabrowa.distributed.zad1.protocol.Message;
import com.jdabrowa.distributed.zad1.protocol.MessageReader;
import com.jdabrowa.distributed.zad1.protocol.MessageWriter;
import com.jdabrowa.distributed.zad1.protocol.OutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractWorkerRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWorkerRunnable.class);

    private final MessageWriter writer;
    private final MessageReader reader;
    private AfterActionCallback callback;

    public AbstractWorkerRunnable(MessageReader reader, MessageWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void setAfterActionCallback(AfterActionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            receiveAndReply();
        } catch (IOException ioException) {
            // TODO: Log
        } finally {
            executeCallback();
        }
    }

    private void executeCallback() {
        if(callback != null) {
            callback.cleanUp();
        }
    }


    private void receiveAndReply() throws IOException {
        Message message = reader.readMessage();
        int numberOfOrderedDigit = message.getFourByteNumber();
        OutgoingMessage outMessage = getOutMessage(numberOfOrderedDigit);
        writer.sendMessage(outMessage);
    }

    protected abstract OutgoingMessage getOutMessage(int n);

    public static interface AfterActionCallback {
        public void cleanUp();
    }
}
