package com.jdabrowa.distributed.zad1.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SingleDigitMessage implements OutgoingMessage {

    private final int piDigit;

    public SingleDigitMessage(int piDigit) {
        this.piDigit = piDigit;
    }

    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            new DataOutputStream(bos).writeInt(piDigit);
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
