package pl.jdabrowa.agh.distributed.ice.server;

import Ice.Communicator;
import Ice.ObjectAdapter;
import pl.jdabrowa.agh.distributed.ice.error.IceError;

public class SimpleServer {

    private static final String ICE_OBJECT_ADAPTER_PROPERTY_KEY = "pl.jdabrowa.agh.distributed.ice.server.SimpleServer.Adapter";
    private final ObjectAdapter objectAdapter;


    private SimpleServer(String ... args) throws IceError {
        Communicator iceCommunicator = Ice.Util.initialize(args);
        this.objectAdapter = iceCommunicator.createObjectAdapter(ICE_OBJECT_ADAPTER_PROPERTY_KEY);
        validateAdapterNotNull();

    }

    private void validateAdapterNotNull() throws IceError {
        if(null == this.objectAdapter) {
            String errorMessage = String.format("ObjectAdapter initialization error - adapter specified by property '%s' is null",
                    ICE_OBJECT_ADAPTER_PROPERTY_KEY);
            throw new IceError(errorMessage);
        }
    }

    public static void main(String[] args) {

    }
}
