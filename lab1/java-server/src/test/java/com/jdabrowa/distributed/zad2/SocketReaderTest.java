package com.jdabrowa.distributed.zad2;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class SocketReaderTest {

    private SocketReader instance;
    private Socket mockSocket;

    @Before
    public void setUp() {
        mockSocket = mock(Socket.class);
        instance = new SocketReader(mockSocket);
    }

    @Test
    public void shouldCorrectlyReadFileRequest() throws IOException {

        // given
        prepareSocket();

        // when
        FileRequest request = instance.readRequest();

        // then
        assertEquals("Dupa", request.getFileName());
    }

    private void prepareSocket() throws IOException {
        reset(mockSocket);
        byte[] buf = prepareStreamContent();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
        when(mockSocket.getInputStream()).thenReturn(inputStream);
    }

    private byte[] prepareStreamContent() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(5);
        appendIntegerLiteralFourInNetworkEndianess(bos);
        appendAsciiChars(bos);
        return bos.toByteArray();
    }

    private void appendAsciiChars(ByteArrayOutputStream bos) {
        bos.write(0x44);
        bos.write(0x75);
        bos.write(0x70);
        bos.write(0x61);
    }

    private void appendIntegerLiteralFourInNetworkEndianess(ByteArrayOutputStream bos) {
        bos.write(0x00);
        bos.write(0x00);
        bos.write(0x00);
        bos.write(0x04);
    }

}