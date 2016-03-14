package com.jdabrowa.distributed.zad3;

public class CorruptedMessageException extends Throwable {
    public CorruptedMessageException(String message) {
        super(message);
    }
}
