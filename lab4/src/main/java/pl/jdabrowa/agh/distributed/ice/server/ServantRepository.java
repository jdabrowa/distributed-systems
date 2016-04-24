package pl.jdabrowa.agh.distributed.ice.server;

import Ice.Object;

public interface ServantRepository {
    Object getServant(String ID);
}
