package pl.jdabrowa.distributed.jms.client.error;

public class MessagingException extends Exception {

    public MessagingException(String cause) {
        super(cause);
    }

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
