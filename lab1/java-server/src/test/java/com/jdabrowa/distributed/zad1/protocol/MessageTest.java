package com.jdabrowa.distributed.zad1.protocol;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void shouldPropagateParametersFromConstructorToVariables() {

        // given
        byte b = (byte) -100;
        short twoByteNumber = -22123;

        // when
        Message message = new Message(b, twoByteNumber, 2000000000, 15L);

        // then
        assertEquals(-100, message.getOneByteNumber());
        assertEquals(-22123, message.getTwoByteNumber());
        assertEquals(2000000000, message.getFourByteNumber());
        assertEquals(15L, message.getEightByteNumber());
    }
}