package com.jdabrowa.distributed.zad1.protocol;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void shouldPropagateParametersFromConstructorToVariables() {

        // given
        byte b = (byte) -100;

        // when
        Message message = new Message(b, -22123, 2000000000, 15L);

        // then
        assertEquals(-100, message.getOneByteNumber());
        assertEquals(-22123, message.getTwoByteNumber());
        assertEquals(2000000000, message.getFourByteNumber());
        assertEquals(15L, message.getEightByteNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnValueAboveSigned2BytesRange() {

        // given
        int valueAboveSigned2BytesRange = 32768;

        // when
        new Message((byte) 0, valueAboveSigned2BytesRange, 0, 0);

        // then
        fail("Exception should have been thrown");

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnValueBelowSigned2BytesRange() {

        // given
        int valueBelowSigned2BytesRange = -32769;

        // when
        new Message((byte) 0, valueBelowSigned2BytesRange, 0, 0);

        // then
        fail("Exception should have been thrown");

    }

}