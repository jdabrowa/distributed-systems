package pl.jdabrowa.distributed.jms.client.tasks;

public class MessageSendTask implements SendTask {

    private final String uniqueId;

    public MessageSendTask(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public void run() {

    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
}