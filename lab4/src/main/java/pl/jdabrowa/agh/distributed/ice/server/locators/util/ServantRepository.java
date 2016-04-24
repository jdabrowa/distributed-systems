package pl.jdabrowa.agh.distributed.ice.server.locators.util;

import Ice.Object;

public interface ServantRepository {
    Object getServant(String ID);
}
