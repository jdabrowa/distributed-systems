package pl.jdabrowa.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.jms.server.ServerHandler;
import pl.jdabrowa.distributed.jms.server.error.ServerException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

public class EchoServerHandler implements ServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler.class);

    @Override
    public void handleMessage(BytesMessage request, BytesMessage response) {

        try {
            handleInternal(request, response);
        } catch (ServerException | JMSException e) {
            LOGGER.warn("Error occurred while handling request", e);
        }

    }

    private void handleInternal(BytesMessage request, BytesMessage response) throws JMSException, ServerException {
        int bodyLength = (int) request.getBodyLength();
        byte [] requestBytes = new byte[bodyLength];
        int bytesRead = request.readBytes(requestBytes);
        if(bytesRead != bodyLength) {
            throw new ServerException("Failed to read whole request", null);
        }
        String message = new String(requestBytes);
        String reply = "Echo: " + message;
        LOGGER.info("Incoming request: {}", message);
        response.writeBytes(reply.getBytes());
    }
}
