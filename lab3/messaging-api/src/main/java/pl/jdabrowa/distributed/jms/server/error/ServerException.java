package pl.jdabrowa.distributed.jms.server.error;

public class ServerException extends Exception {
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServerException(Throwable cause) {
        super(cause);
    }
}
