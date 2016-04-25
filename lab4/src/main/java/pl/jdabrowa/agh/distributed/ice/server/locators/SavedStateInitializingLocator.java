package pl.jdabrowa.agh.distributed.ice.server.locators;

import Ice.*;
import Ice.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.server.locators.util.ServantRepository;

import java.util.function.Supplier;

public class SavedStateInitializingLocator extends LocatorBase {

    private final ServantRepository servantRepository;

    public SavedStateInitializingLocator(ServantRepository repository) {
        LOGGER.trace("Instantiating {}", this.getClass().getSimpleName());
        this.servantRepository = repository;
    }

    private Supplier<Object> servantProvider = this::virtualProxy;

    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        LOGGER.trace("Received request for persisted servant");
        Identity id = current.id;
        LOGGER.trace("Request params: category - {}, name - {}", id.category, id.name);
        Object servant = servantProvider.get();
        current.adapter.add(servant, id);
        return servant;
    }

    private synchronized Object virtualProxy() {

        class ObjectFactory implements Supplier<Object> {

            private static final String SERVANT_ID = "pl.jdabrowa.agh.distributed.ice.server.locators.SavedStateInitializingLocator.servant";

            private final Object servant;

            private ObjectFactory(ServantRepository repository) {
                LOGGER.debug("Loading servant from repository");
                this.servant = repository.getServant(SERVANT_ID);
                LOGGER.debug("Servant loaded");
            }

            @Override
            public Object get() {
                return this.servant;
            }
        }

        if(!ObjectFactory.class.isInstance(this.servantProvider)) {
            LOGGER.debug("Lazy initialization of persisted servant start...");
            servantProvider = new ObjectFactory(servantRepository);
            LOGGER.debug("Lazy initialization of persisted servant completed");
        }
        return servantProvider.get();
    }

}
