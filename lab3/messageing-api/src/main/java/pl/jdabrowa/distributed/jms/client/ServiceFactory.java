package pl.jdabrowa.distributed.jms.client;

public interface ServiceFactory {
    Service createService(ServiceConfiguration configuration);
}
