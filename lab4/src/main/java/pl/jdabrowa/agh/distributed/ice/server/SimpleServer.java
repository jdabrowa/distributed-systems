package pl.jdabrowa.agh.distributed.ice.server;

import Ice.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.server.locators.PooledLocator;
import pl.jdabrowa.agh.distributed.ice.util.IceConfigurator;
import pl.jdabrowa.agh.distributed.ice.error.IceError;
import pl.jdabrowa.agh.distributed.ice.server.configuration.CategoryConstants;
import pl.jdabrowa.agh.distributed.ice.server.locators.PerRequestLocator;
import pl.jdabrowa.agh.distributed.ice.server.locators.SavedStateInitializingLocator;
import pl.jdabrowa.agh.distributed.ice.server.locators.util.ServantRepository;

public class SimpleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private static final String ICE_OBJECT_ADAPTER_PROPERTY_KEY = "pl.jdabrowa.agh.distributed.ice.server.SimpleServer.Adapter";
    private static final String CONFIGURATION_FILE_NAME = "ice-server.properties";
    private final ObjectAdapter objectAdapter;

    private SimpleServer(String ... args) throws IceError {

        String [] iceArgs = new IceConfigurator().addConfiguration(args, CONFIGURATION_FILE_NAME);

        Communicator iceCommunicator = Ice.Util.initialize(iceArgs);
        this.objectAdapter = iceCommunicator.createObjectAdapter(ICE_OBJECT_ADAPTER_PROPERTY_KEY);
        validateAdapterNotNull();
        setLocators();
        addDefaultServants();
        objectAdapter.activate();
        LOGGER.info("Server awaiting requests...");
        iceCommunicator.waitForShutdown();
    }

    private void addDefaultServants() {
        LOGGER.trace("Setting default servant for category 4");
        Ice.Object defaultServant = new SimpleOperationServant();
        objectAdapter.addDefaultServant(defaultServant, CategoryConstants.DEFAULT_SERVANT);
    }

    private void setLocators() {

        ServantRepository repository = new MockRepository();
        ServantLocator restoringPersistedServantLocator = new SavedStateInitializingLocator(repository);
        setLocatorForCategory(restoringPersistedServantLocator, CategoryConstants.RESTORE_PERSISTED);

        ServantLocator perRequestLocator = new PerRequestLocator();
        setLocatorForCategory(perRequestLocator, CategoryConstants.NEW_SERVANT_PER_REQUEST);

        ServantLocator lruLocator = new PooledLocator(5);
        setLocatorForCategory(lruLocator, CategoryConstants.LRU_SERVANT_POOL);
    }

    private void setLocatorForCategory(ServantLocator locator, String category) {
        LOGGER.debug("Setting Servant Locator for category {}", category);
        objectAdapter.addServantLocator(locator, category);
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
