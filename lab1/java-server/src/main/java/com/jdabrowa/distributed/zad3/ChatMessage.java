package com.jdabrowa.distributed.zad3;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class ChatMessage {

    public static final int MESSAGE_LENGTH_IN_BYTES = 6 + 20 + 8;
    public static final int CRC_LENGTH = 8;
    public static final int BYTES_PER_NICK_IN_ASCII = 6;
    public static final int BYTES_PER_TEXT_IN_ASCII = 20;

    @Getter private final Date date;
    @Getter private final String nickName;
    @Getter private final String message;

    public ChatMessage(Date date, String nickName, String message) {
        this.date = date;
        this.nickName = nickName;
        this.message = message;
    }

    public byte [] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(MESSAGE_LENGTH_IN_BYTES);
        serializeMessageContentToStream(bos);
        return bos.toByteArray();
    }

    private void serializeMessageContentToStream(ByteArrayOutputStream bos) throws IOException {
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeLong(date.getTime());
        writeStringAsAsciiSequenceWithPadding(nickName, dos, ChatMessage.BYTES_PER_NICK_IN_ASCII);
        writeStringAsAsciiSequenceWithPadding(message, dos, ChatMessage.BYTES_PER_TEXT_IN_ASCII);
    }

    private void writeStringAsAsciiSequenceWithPadding(String s, DataOutputStream dos, int length) throws IOException {
        int count = 0;
        for(char c : s.toCharArray()) {
            dos.writeByte((byte) c);
            ++count;
        }
        while(count < length) {
            dos.writeByte((byte) 0);
            ++count;
        }
    }
}
