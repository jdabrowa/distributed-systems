package com.jdabrowa.distributed.zad1.protocol;

import java.io.IOException;

public interface MessageWriter {
    public void sendMessage(OutgoingMessage message) throws IOException;
}
