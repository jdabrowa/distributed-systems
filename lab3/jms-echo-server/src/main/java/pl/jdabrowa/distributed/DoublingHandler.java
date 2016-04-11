package pl.jdabrowa.distributed;

public class DoublingHandler extends ServerHandlerBase {

    @Override
    protected String process(String request) {
        return request + " " + request;
    }
}
