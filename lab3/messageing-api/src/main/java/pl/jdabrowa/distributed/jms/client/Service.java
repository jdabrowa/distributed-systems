package pl.jdabrowa.distributed.jms.client;

public interface Service {
    byte [] sendAndReceive(byte [] requestPayload);
}
