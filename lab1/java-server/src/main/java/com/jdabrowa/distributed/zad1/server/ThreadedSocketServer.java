package com.jdabrowa.distributed.zad1.server;

import com.jdabrowa.distributed.zad1.protocol.SocketReader;
import com.jdabrowa.distributed.zad1.protocol.SocketWriter;
import com.jdabrowa.distributed.zad1.server.AbstractWorkerRunnable.AfterActionCallback;
import com.jdabrowa.distributed.zad1.service.Rfc3091Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

public class ThreadedSocketServer implements Rfc3091Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedSocketServer.class);

    private final Executor executor;
    private Rfc3091Service service = null;
    private ServerSocket piSocket;
    private ServerSocket approx22by7Socket;

    private volatile boolean shouldRun;

    public ThreadedSocketServer(Executor executor ,Rfc3091Service service) {
        this.executor = executor;
        this.service = service;
    }

    @Override
    public void bindPiDigitGeneration(int portNumber) throws IOException {
        this.piSocket = new ServerSocket(portNumber);
    }

    @Override
    public void bind22by7DigitGeneration(int portNumber) throws IOException {
        this.approx22by7Socket = new ServerSocket(portNumber);
    }

    @Override
    public void start() {

        validateSocketsInitialized();
        shouldRun = true;
        new Thread(() -> awaitAndHandleRequests(piSocket, piRunnableProvider)).start();
        new Thread(() -> awaitAndHandleRequests(approx22by7Socket, approx22by7Provider)).start();
    }

    private void validateSocketsInitialized() {
        if(!socketsInitialized()) {
            throw new IllegalStateException("Sockets have not been bound to ports");
        }
    }

    private boolean socketsInitialized() {
        return piSocket != null && approx22by7Socket != null;
    }

    private void awaitAndHandleRequests(ServerSocket socket, RunnableProvider provider) {
        while(shouldRun) {
            try {
                handleConnection(socket, provider);
            } catch (IOException e) {
                LOGGER.warn("IOException during request handling", e); // TODO
            }
        }
    }

    private void handleConnection(ServerSocket socket, RunnableProvider provider) throws IOException {
        Socket incomingConnectionSocket = socket.accept();
        LOGGER.debug("Received incoming connection");
        AbstractWorkerRunnable runnableForIncomingConnection = wrapRequestAsRunnable(provider, incomingConnectionSocket);
        executor.execute(runnableForIncomingConnection);
    }

    private AbstractWorkerRunnable wrapRequestAsRunnable(RunnableProvider provider, Socket incomingConnectionSocket) throws IOException {
        AbstractWorkerRunnable runnableForIncomingConnection = createRunnableForIncomingConnection(incomingConnectionSocket, provider);
        AfterActionCallback socketClosingCallback = socketClosingCallbackFor(incomingConnectionSocket);
        runnableForIncomingConnection.setAfterActionCallback(socketClosingCallback);
        return runnableForIncomingConnection;
    }

    private AbstractWorkerRunnable createRunnableForIncomingConnection(Socket incomingConnectionSocket, RunnableProvider provider)
            throws IOException {
        SocketReader reader = new SocketReader(incomingConnectionSocket);
        SocketWriter writer = new SocketWriter(incomingConnectionSocket);
        return provider.createRunnable(reader, writer);
    }

    private AfterActionCallback socketClosingCallbackFor(Socket incomingConnectionSocket) {
        return () -> {
            try {
                incomingConnectionSocket.close();
            } catch (IOException e) {
                LOGGER.warn("IOException occurred during socket connection closing", e); // TODO
            }
        };
    }

    private interface RunnableProvider {
        AbstractWorkerRunnable createRunnable(SocketReader reader, SocketWriter writer) throws IOException;
    }

    private final RunnableProvider piRunnableProvider  = (reader, writer) -> new PiWorkerRunnable(reader, writer, service);
    private final RunnableProvider approx22by7Provider = (reader, writer) -> new The22By7Runnable(reader, writer, service);
}
