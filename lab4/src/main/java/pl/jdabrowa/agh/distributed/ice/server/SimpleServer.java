package pl.jdabrowa.agh.distributed.ice.server;

import Ice.Communicator;
import Ice.ObjectAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.error.IceError;
import pl.jdabrowa.agh.distributed.ice.server.locators.SavedStateInitializingLocator;
import pl.jdabrowa.agh.distributed.ice.server.locators.util.ServantRepository;

public class SimpleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private static final String ICE_OBJECT_ADAPTER_PROPERTY_KEY = "pl.jdabrowa.agh.distributed.ice.server.SimpleServer.Adapter";
    private final ObjectAdapter objectAdapter;

    private SimpleServer(String ... args) throws IceError {
        Communicator iceCommunicator = Ice.Util.initialize(args);
        this.objectAdapter = iceCommunicator.createObjectAdapter(ICE_OBJECT_ADAPTER_PROPERTY_KEY);
        validateAdapterNotNull();
        setLocators();
        objectAdapter.activate();
        LOGGER.info("Server awaiting requests...");
        iceCommunicator.waitForShutdown();
    }

    private void setLocators() {
        LOGGER.debug("Setting Servant Locator for category {}", CategoryConstants.RESTORE_PERSISTED);
        ServantRepository repository = new MockRepository();
        SavedStateInitializingLocator restoringPersistedServantLocator = new SavedStateInitializingLocator(repository);
        objectAdapter.addServantLocator(restoringPersistedServantLocator, CategoryConstants.RESTORE_PERSISTED);
    }

    private void validateAdapterNotNull() throws IceError {
        if(null == this.objectAdapter) {
            String errorMessage = String.format("ObjectAdapter initialization error - adapter specified by property '%s' is null",
                    ICE_OBJECT_ADAPTER_PROPERTY_KEY);
            throw new IceError(errorMessage);
        }
    }

    public static void main(String[] args) throws IceError {
        new SimpleServer(args);
    }
}
