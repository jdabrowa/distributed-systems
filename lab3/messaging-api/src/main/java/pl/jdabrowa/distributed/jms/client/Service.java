package pl.jdabrowa.distributed.jms.client;

import pl.jdabrowa.distributed.jms.client.error.MessagingException;

public interface Service {
    byte [] sendAndReceive(byte [] requestPayload, long timeoutMillis) throws MessagingException;
}
