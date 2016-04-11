package pl.jdabrowa.distributed.jms.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.jms.server.error.ServerException;

import javax.jms.*;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final JmsInternals jmsInternals;
    private final ServerHandler handler;

    public Server(String queueName, ServerHandler handler) throws ServerException {
        this.handler = handler;
        try {
            this.jmsInternals = new JmsInternals(queueName);
            jmsInternals.getConsumer().setMessageListener(new InternalHandler());
        } catch (JMSException e) {
            throw new ServerException("Exception during server setup", e);
        }
    }

    private class InternalHandler implements MessageListener {

        @Override
        public void onMessage(Message message) {
            try {
                handleInternal((BytesMessage) message);
            } catch (JMSException e) {
                LOGGER.warn("Exception while handling message", e);
            }
        }

        private void handleInternal(BytesMessage requestMessage) throws JMSException {
            String requestCorrelationID = requestMessage.getJMSCorrelationID();
            BytesMessage responseMessage = Server.this.jmsInternals.createMessage();
            responseMessage.setJMSCorrelationID(requestCorrelationID);
            Destination replyTo = requestMessage.getJMSReplyTo();
            handler.handleMessage(requestMessage, responseMessage);
            Server.this.jmsInternals.getProducer().send(replyTo, responseMessage);
        }
    }

}
