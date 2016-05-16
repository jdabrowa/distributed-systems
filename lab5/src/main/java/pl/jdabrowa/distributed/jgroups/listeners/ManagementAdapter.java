package pl.jdabrowa.distributed.jgroups.listeners;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatState;
import pl.jdabrowa.distributed.jgroups.state.ChannelState;
import pl.jdabrowa.distributed.jgroups.state.UserRepository;

import java.io.InputStream;
import java.io.OutputStream;

public class ManagementAdapter extends ReceiverAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementAdapter.class);

    private final UserRepository userRepository;

    public ManagementAdapter(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public void receive(Message msg) {
        try {
            ChatAction chatAction = ChatAction.parseFrom(msg.getBuffer());
            synchronized (userRepository) {
                dispatchAction(chatAction);
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warn("Cannot parse message", e);
        }
    }

    @Override
    public void getState(OutputStream outputStream) throws Exception {
        synchronized (userRepository) {
            ChatState.Builder chatStateBuilder = ChatState.newBuilder();
            appendAllRegisteredUsersAsJoinActions(chatStateBuilder);
            ChatState currentChatState = chatStateBuilder.build();
            outputStream.write(currentChatState.toByteArray());
        }
    }

    @Override
    public void setState(InputStream inputStream) throws Exception {
        synchronized (userRepository) {
            ChatState newState = ChatState.parseFrom(inputStream);
            userRepository.getChannelStateMap().clear();
            newState.getStateList().forEach(userRepository::doJoin);
            LOGGER.info("Received state information");
        }
    }

    private void appendAllRegisteredUsersAsJoinActions(ChatState.Builder chatStateBuilder) {
        for(ChannelState state : userRepository.getChannelStateMap().values()) {
            for(String userName : state.getUsers()) {
                appendAsJoinAction(chatStateBuilder, state, userName);
            }
        }
    }

    private void appendAsJoinAction(ChatState.Builder chatStateBuilder, ChannelState state, String userName) {
        ChatAction singleJoinAction = ChatAction.newBuilder()
                .setAction(ChatAction.ActionType.JOIN)
                .setChannel(state.getName())
                .setNickname(userName)
                .build();
        chatStateBuilder.addState(singleJoinAction);
    }

    private void dispatchAction(ChatAction action) {

        switch (action.getAction()) {
            case JOIN:
                userRepository.doJoin(action);
                break;

            case LEAVE:
                userRepository.doLeave(action);
                break;
        }
    }
}
