package pl.jdabrowa.agh.distributed.ice.server.locators;

import Ice.*;
import org.slf4j.*;

public abstract class LocatorBase implements ServantLocator {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SavedStateInitializingLocator.class);

    @Override
    public void finished(Current current, Ice.Object object, java.lang.Object o) throws UserException {
        logMethodCall("finished");
    }

    @Override
    public void deactivate(String s) {
        logMethodCall("deactivate");
    }

    private void logMethodCall(String methodName) {
        LOGGER.trace("Received call to '{}()' method on {}", methodName, this.getClass().getSimpleName());
    }
}
