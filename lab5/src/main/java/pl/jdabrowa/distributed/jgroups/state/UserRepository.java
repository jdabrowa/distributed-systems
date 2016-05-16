package pl.jdabrowa.distributed.jgroups.state;

import lombok.Getter;
import org.springframework.stereotype.Component;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;
import pl.jdabrowa.distributed.jgroups.state.ChannelState;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {

    @Getter
    private final Map<String, ChannelState> channelStateMap;

    public UserRepository() {
        channelStateMap = new HashMap<>();
    }


    public void doJoin(ChatOperationProtos.ChatAction action) {
        ensureStateInMap(action.getChannel());
        registerUserForChannel(action.getChannel(), action.getNickname());
    }

    public void removeUserFromChannel(String channelName, String senderNickName) {
        ChannelState state = channelStateMap.get(channelName);
        state.deleteUser(senderNickName);
        if(state.isEmpty()) {
            channelStateMap.remove(channelName);
        }
    }

    public boolean stateInMap(String channelName) {
        return channelStateMap.containsKey(channelName);
    }

    public void registerUserForChannel(String channelName, String senderNickName) {
        ChannelState channelState = channelStateMap.get(channelName);
        if(!channelState.hasUser(senderNickName)) {
            channelState.addUser(senderNickName);
        }
    }

    public void ensureStateInMap(String channelName) {
        if(!stateInMap(channelName)) {
            channelStateMap.put(channelName, new ChannelState(channelName));
        }
    }

    public void doLeave(ChatOperationProtos.ChatAction action) {
        if(stateInMap(action.getChannel())) {
            removeUserFromChannel(action.getChannel(), action.getNickname());
        }
    }
}
