package pl.jdabrowa.distributed.jgroups.chat;

import org.jgroups.JChannel;

public interface IChat {
    void joinChannel(String channelNumber) throws Exception;
    void sendMessage(String message) throws Exception;
    JChannel createAndJoinManagementChannel() throws Exception;
    void leaveChannel(String channelNumber) throws Exception;
}
