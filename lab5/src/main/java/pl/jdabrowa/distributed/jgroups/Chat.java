package pl.jdabrowa.distributed.jgroups;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.stack.ProtocolStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatMessage;

import java.util.HashSet;
import java.util.Set;

@Component
public class Chat implements IChat {

    private static final String USE_DEFAULT_MULTICAST_ADDRESS = "";

    private final JGroups jGroups;
    private final Set<JChannel> chatChannels;
    private final JChannel managementChannel;

    @Value("${chat.client.nickname}")
    private String nickName;

    @Autowired
    public Chat(JGroups jGroups) {
        this.jGroups = jGroups;
        this.chatChannels = new HashSet<>();
    }

    @Override
    public void joinChannel(String channelNumber) throws Exception {
        JChannel channel = jGroups.newJChannel();
        ProtocolStack protocolStack = jGroups.createRequiredProtocolStack(channelNumber);
        pairChannelAndProtocolStack(channel, protocolStack);
        channel.setName(nickName);
        channel.setReceiver(jGroups.newReceiverFor(channel));
        channel.connect(channelNumber);
        chatChannels.add(channel);
    }

    @Override
    public void sendMessage(String messageText) throws Exception {
        ChatMessage chatMessage = ChatMessage.newBuilder()
                .setMessage(messageText)
                .build();

        for(JChannel channel : chatChannels) {
            channel.send(new Message(null, null, chatMessage.toByteArray()));
        }
    }

    @Override
    public void joinManagementChannel() throws Exception {
        JChannel managementChannel = jGroups.newJChannel();
        ProtocolStack protocolStack = jGroups.createRequiredProtocolStack(USE_DEFAULT_MULTICAST_ADDRESS);
        pairChannelAndProtocolStack(managementChannel, protocolStack);

    }

    private void pairChannelAndProtocolStack(JChannel channel, ProtocolStack protocolStack) throws Exception {
        protocolStack.setChannel(channel);
        protocolStack.init();
        channel.setProtocolStack(protocolStack);
    }
}
