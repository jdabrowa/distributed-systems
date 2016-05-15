package pl.jdabrowa.agh.distributed.ice.server.locators;

import Ice.*;
import Ice.Object;
import pl.jdabrowa.agh.distributed.ice.server.SimpleOperationServant;

public class PerRequestLocator extends LocatorBase {

    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        LOGGER.trace("Received locate() request. Category: {}, name: {}", current.id.category, current.id.name);
        return new SimpleOperationServant();
    }
}
