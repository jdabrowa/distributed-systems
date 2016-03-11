package com.jdabrowa.distributed.zad1.protocol;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocketReaderTest {

    @Test
    public void shouldCorrectlyDeserializeMessage() throws IOException {

        // given
        Socket mockSocket = createMockSocket();
        SocketReader instance = new SocketReader(mockSocket);

        // when
        Message message = instance.readMessage();

        // then
        assertEquals(25, message.getOneByteNumber());
        assertEquals(20, message.getTwoByteNumber());
        assertEquals(15, message.getFourByteNumber());
        assertEquals(10L, message.getEightByteNumber());
    }

    private Socket createMockSocket() throws IOException {
        Socket mockSocket = mock(Socket.class);
        byte[] bytes = prepareBytesAsReceivedFromNetwork();
        ByteArrayInputStream preparedStream = new ByteArrayInputStream(bytes);
        when(mockSocket.getInputStream()).thenReturn(preparedStream);
        return mockSocket;
    }

    private byte[] prepareBytesAsReceivedFromNetwork() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(Message.LENGTH_IN_BYTES);
        DataOutputStream dataOutputStream = new DataOutputStream(bos);
        dataOutputStream.writeByte(25);
        dataOutputStream.writeShort(20);
        dataOutputStream.writeInt(15);
        dataOutputStream.writeLong(10L);
        return bos.toByteArray();
    }

}