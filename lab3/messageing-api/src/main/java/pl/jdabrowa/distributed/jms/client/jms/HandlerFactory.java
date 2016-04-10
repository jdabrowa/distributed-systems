package pl.jdabrowa.distributed.jms.client.jms;

import javax.jms.MessageListener;

public class HandlerFactory {
    public MessageListener createListener() {
        return new PerThreadHandler();
    }
}
