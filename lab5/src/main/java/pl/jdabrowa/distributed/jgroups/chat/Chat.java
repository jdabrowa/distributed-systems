package pl.jdabrowa.distributed.jgroups.chat;

import lombok.Getter;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.stack.ProtocolStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatMessage;
import pl.jdabrowa.distributed.jgroups.JGroups;
import pl.jdabrowa.distributed.jgroups.configuration.ChatClientConfiguration;
import pl.jdabrowa.distributed.jgroups.state.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Chat implements IChat {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chat.class);

    private static final String USE_DEFAULT_MULTICAST_ADDRESS = "";
    private static final String MANAGEMENT_CHANNEL_NAME = "ChatManagement321123";
    public static final long TIMEOUT_SECONDS = 10;

    private final JGroups jGroups;
    private final Map<String, JChannel> chatChannels;
    private final JChannel managementChannel;

    @Getter
    private final UserRepository userRepository;

    private final String nickName;

    @Autowired
    public Chat(JGroups jGroups, UserRepository userRepository, ChatClientConfiguration configuration) throws Exception {
        this.jGroups = jGroups;
        this.chatChannels = new HashMap<>();
        nickName = configuration.getUserName();
        this.userRepository = userRepository;
        this.managementChannel = createAndJoinManagementChannel();
    }

    @Override
    public void joinChannel(String channelNumber) throws Exception {
        createAndConnectToChannel(channelNumber);
        registerAndBroadcastJoin(channelNumber);
    }

    @Override
    public void sendMessage(String messageText) throws Exception {
        ChatMessage chatMessage = ChatMessage.newBuilder()
                .setMessage(messageText)
                .build();

        for(JChannel channel : chatChannels.values()) {
            channel.send(new Message(null, null, chatMessage.toByteArray()));
        }
    }

    @Override
    public JChannel createAndJoinManagementChannel() throws Exception {
        JChannel managementChannel = jGroups.newJChannel();
        ProtocolStack protocolStack = jGroups.createRequiredProtocolStack(USE_DEFAULT_MULTICAST_ADDRESS);
        pairChannelAndProtocolStack(managementChannel, protocolStack);
        managementChannel.setName(nickName);
        managementChannel.setReceiver(jGroups.newManagementChannelReceiver(userRepository));
        managementChannel.connect(MANAGEMENT_CHANNEL_NAME);
        managementChannel.getState(null, TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
        LOGGER.info("Users state: {}", userRepository.getChannelStateMap().toString());
        return managementChannel;
    }

    @Override
    public void leaveChannel(String channelNumber) throws Exception {
        JChannel channel = chatChannels.get(channelNumber);
        if(null != channel) {
            registerAndBroadcastLeave(channelNumber);
            chatChannels.remove(channelNumber);
        }
    }

    private void createAndConnectToChannel(String channelNumber) throws Exception {
        JChannel channel = jGroups.newJChannel();
        ProtocolStack protocolStack = jGroups.createRequiredProtocolStack(channelNumber);
        pairChannelAndProtocolStack(channel, protocolStack);
        channel.setName(nickName);
        channel.setReceiver(jGroups.newReceiverFor(channel));
        channel.connect(channelNumber);
        chatChannels.put(channelNumber, channel);
    }

    private void registerAndBroadcastJoin(String channelNumber) throws Exception {
        ChatAction chatAction = ChatAction.newBuilder()
                .setAction(ChatAction.ActionType.JOIN)
                .setNickname(nickName)
                .setChannel(channelNumber)
                .build();

        Message message = new Message(null, null, chatAction.toByteArray());
        synchronized (userRepository) {
            userRepository.doJoin(chatAction);
        }
        managementChannel.send(message);
    }

    private void registerAndBroadcastLeave(String channelNumber) throws Exception {
        ChatAction chatAction = ChatAction.newBuilder()
                .setAction(ChatAction.ActionType.LEAVE)
                .setNickname(nickName)
                .setChannel(channelNumber)
                .build();

        Message message = new Message(null, null, chatAction.toByteArray());
        synchronized (userRepository) {
            userRepository.doLeave(chatAction);
        }
        managementChannel.send(message);
    }

    private void pairChannelAndProtocolStack(JChannel channel, ProtocolStack protocolStack) throws Exception {
        protocolStack.setChannel(channel);
        channel.setProtocolStack(protocolStack);
        protocolStack.init();
    }
}
