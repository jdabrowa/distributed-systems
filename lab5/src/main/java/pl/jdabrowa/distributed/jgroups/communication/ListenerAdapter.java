package pl.jdabrowa.distributed.jgroups.communication;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatMessage;

public class ListenerAdapter extends ReceiverAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerAdapter.class);
    private final JChannel channel;

    public ListenerAdapter(JChannel channel) {
        this.channel = channel;
    }

    @Override
    public void receive(Message msg) {
        Address sourceAddress = msg.getSrc();
        byte[] buffer = msg.getBuffer();

        ChatMessage message;
        try {
            message = ChatMessage.parseFrom(buffer);
            String messageText = message.getMessage();
            String sender = channel.getName(sourceAddress);
            LOGGER.info("{}> {}", sender, messageText);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warn("Message parsing error", e);
        }
    }
}
