package com.jdabrowa.distributed.zad1.protocol;

import lombok.Getter;

public class Message {

    public static final int LENGTH_IN_BYTES = 1 + 2 + 4 + 8;

    @Getter private final byte oneByteNumber;
    @Getter private final int twoByteNumber;
    @Getter private final int fourByteNumber;
    @Getter private final long eightByteNumber;

    public Message(byte oneByteNumber, short twoByteNumber, int fourByteNumber, long eightByteNumber) {

        this.oneByteNumber = oneByteNumber;
        this.twoByteNumber = twoByteNumber;
        this.fourByteNumber = fourByteNumber;
        this.eightByteNumber = eightByteNumber;
    }
}
