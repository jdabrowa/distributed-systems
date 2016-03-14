package com.jdabrowa.distributed.zad3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.CRC32;

public class MessageReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReader.class);

    private final DatagramSocket socket;

    public MessageReader(DatagramSocket socket) {
        this.socket = socket;
    }

    public ChatMessage readMessage() throws IOException, CorruptedMessageException {

        int totalBytesToReceive = ChatMessage.MESSAGE_LENGTH_IN_BYTES + ChatMessage.CRC_LENGTH;
        byte [] packetBuffer = new byte [totalBytesToReceive];
        DatagramPacket packet = new DatagramPacket(packetBuffer, packetBuffer.length);
        socket.receive(packet);
        LOGGER.debug("Incoming packet length: {}, sender: {}, port: {}", packet.getLength(), packet.getAddress().toString(), packet.getPort());
        InputStream inputStream = new ByteArrayInputStream(packet.getData());
        byte[] messageBytes = readMessageBytes(inputStream);
        LOGGER.debug("Bytes received: {}", Arrays.toString(messageBytes));

        validateUsingChecksums(inputStream, messageBytes);
        return createMessageFromBytes(messageBytes);
    }

    private void validateUsingChecksums(InputStream inputStream, byte[] messageBytes) throws IOException, CorruptedMessageException {
        long checksumFromMessage = new DataInputStream(inputStream).readLong();
        long calculatedChecksum = calculateChecksum(messageBytes);

        validateChecksumsEqual(checksumFromMessage, calculatedChecksum);
    }

    private byte[] readMessageBytes(InputStream inputStream) throws IOException {
        int bytesRead = 0;
        byte [] messageBytes = new byte[ChatMessage.MESSAGE_LENGTH_IN_BYTES];
        while(bytesRead < ChatMessage.MESSAGE_LENGTH_IN_BYTES) {
            bytesRead += inputStream.read(messageBytes, bytesRead, ChatMessage.MESSAGE_LENGTH_IN_BYTES - bytesRead);
        }
        return messageBytes;
    }

    private long calculateChecksum(byte[] messageBytes) {
        CRC32 crc = new CRC32();
        crc.update(messageBytes);
        return crc.getValue();
    }

    private void validateChecksumsEqual(long checkusumFromMessage, long calculatedChecksum) throws CorruptedMessageException {
        if(checkusumFromMessage != calculatedChecksum) {
            LOGGER.debug("Error - calculated checksum was {}, but read {} from network", calculatedChecksum, checkusumFromMessage);
            throw new CorruptedMessageException(String.format("Received corrupted message (checksum mismatch)"));
        }
    }

    private ChatMessage createMessageFromBytes(byte[] messageBytes) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(messageBytes));
        long timestamp = dis.readLong();
        String nick = readNick(dis);
        String messageText = readMessageText(dis);

        Date messageDate = new Date(timestamp);

        return new ChatMessage(messageDate, nick, messageText);
    }

    private String readMessageText(DataInputStream dis) throws IOException {
        StringBuffer buffer;

        buffer = new StringBuffer(ChatMessage.BYTES_PER_TEXT_IN_ASCII);
        for(int i = 0; i < ChatMessage.BYTES_PER_TEXT_IN_ASCII; ++i) {
            buffer.append((char) dis.readByte());
        }
        return buffer.toString();
    }

    private String readNick(DataInputStream dis) throws IOException {
        StringBuffer buffer = new StringBuffer(ChatMessage.BYTES_PER_NICK_IN_ASCII);
        for(int i = 0; i < ChatMessage.BYTES_PER_NICK_IN_ASCII; ++i) {
            buffer.append((char) dis.readByte());
        }
        return buffer.toString();
    }
}
