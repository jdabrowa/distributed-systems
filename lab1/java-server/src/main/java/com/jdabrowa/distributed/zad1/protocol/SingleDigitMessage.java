package com.jdabrowa.distributed.zad1.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
            byte[] bytes = bos.toByteArray();
//            reverse(bytes);
            System.out.println(Arrays.toString(bytes));
            return bytes;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private void reverse(byte [] bytes) {
        for (int i = 0; i <= bytes.length/2; i++) {
            byte tmp = bytes[i];
            int index = bytes.length - i - 1;
            bytes[i] = bytes[index];
            bytes[index] = tmp;
        }
    }
}
