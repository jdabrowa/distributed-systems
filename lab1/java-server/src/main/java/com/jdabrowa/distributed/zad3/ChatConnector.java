package com.jdabrowa.distributed.zad3;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.zip.CRC32;

public class ChatConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatConnector.class);

    private final MulticastSocket socket;
    private final InetAddress groupAddress;
    private final int portNumber;
    private final MessageReader reader;

    private boolean connected = false;

    public ChatConnector(InetAddress groupAddress, int portNumber, MulticastSocket socket, MessageReader reader) throws IOException {

        this.socket = socket;
        this.portNumber = portNumber;
        this.groupAddress = groupAddress;
        this.reader = reader;

        socket.joinGroup(groupAddress);
    }

    public void sendMessage(ChatMessage message) throws IOException {
        byte [] messageBytes = message.getBytes();
        byte [] messageWithCrc = appendChecksum(messageBytes);
        DatagramPacket messagePacket = new DatagramPacket(messageWithCrc, 0, groupAddress, portNumber);
        LOGGER.debug("Sending packet with body: {}", Arrays.toString(messagePacket.getData()));
        socket.send(messagePacket);
    }

    private byte[] appendChecksum(byte[] messageBytes) throws IOException {
        int newSize = messageBytes.length + ChatMessage.CRC_LENGTH;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(newSize);
        bos.write(messageBytes);
        long checksum = calculateChecksum(messageBytes);
        new DataOutputStream(bos).writeLong(checksum);
        return bos.toByteArray();
    }

    private long calculateChecksum(byte[] messageBytes) {
        CRC32 crc = new CRC32();
        crc.update(messageBytes);
        return crc.getValue();
    }

    public ChatMessage receiveMessage() throws IOException, CorruptedMessageException {
        return reader.readMessage();
    }
}
