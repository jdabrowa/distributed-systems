package pl.jdabrowa.agh.distributed.ice.server.locators;

import Ice.*;
import Ice.Object;

public class SavedStateInitializingLocator implements ServantLocator {



    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        return null;
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }
}
