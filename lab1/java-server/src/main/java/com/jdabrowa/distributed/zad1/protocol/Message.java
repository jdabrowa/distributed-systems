package com.jdabrowa.distributed.zad1.protocol;

import lombok.Getter;

public class Message {

    private static final int TWO_BYTES_SIGNED_MAX = 32767;
    private static final int TWO_BYTES_SIGNED_MIN = -32768;

    @Getter private final byte oneByteNumber;
    @Getter private final int twoByteNumber;
    @Getter private final int fourByteNumber;
    @Getter private final long eightByteNumber;

    public Message(byte oneByteNumber, int twoByteNumber, int fourByteNumber, long eightByteNumber) {

        verifyIsTwoBytes(twoByteNumber);

        this.oneByteNumber = oneByteNumber;
        this.twoByteNumber = twoByteNumber;
        this.fourByteNumber = fourByteNumber;
        this.eightByteNumber = eightByteNumber;
    }

    private void verifyIsTwoBytes(int twoByteNumber) {
        if(!isTwoBytes(twoByteNumber)) {
            throw new IllegalArgumentException("Provided number is not two bytes");
        }
    }

    private boolean isTwoBytes(int twoByteNumber) {
        return twoByteNumber <= TWO_BYTES_SIGNED_MAX
                && twoByteNumber >= TWO_BYTES_SIGNED_MIN;
    }
}
