package pl.jdabrowa.distributed;

public class EchoServerHandler extends ServerHandlerBase {
    @Override
    protected String process(String request) {
        return "Echo: " + request;
    }
}
