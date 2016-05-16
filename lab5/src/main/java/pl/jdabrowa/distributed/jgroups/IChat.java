package pl.jdabrowa.distributed.jgroups;

public interface IChat {
    void joinChannel(String channelNumber) throws Exception;
    void sendMessage(String message) throws Exception;
    void joinManagementChannel() throws Exception;
}
