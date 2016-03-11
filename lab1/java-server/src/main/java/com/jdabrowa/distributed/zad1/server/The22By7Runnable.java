package com.jdabrowa.distributed.zad1.server;

import com.jdabrowa.distributed.zad1.protocol.MessageReader;
import com.jdabrowa.distributed.zad1.protocol.MessageWriter;
import com.jdabrowa.distributed.zad1.protocol.OutgoingMessage;
import com.jdabrowa.distributed.zad1.protocol.SingleDigitMessage;
import com.jdabrowa.distributed.zad1.service.Rfc3091Service;

public class The22By7Runnable extends AbstractWorkerRunnable {

    private final Rfc3091Service service;


    public The22By7Runnable(MessageReader reader, MessageWriter writer, Rfc3091Service service) {
        super(reader, writer);
        this.service = service;
    }

    @Override
    protected OutgoingMessage getOutMessage(int n) {
        return new SingleDigitMessage(service.get22by7Nthdigit(n));
    }
}
