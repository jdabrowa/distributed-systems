package pl.jdabrowa.distributed.jms.server;

import javax.jms.BytesMessage;

public interface ServerHandler {

    public void handleMessage(BytesMessage request, BytesMessage response);
}
