package pl.jdabrowa.agh.distributed.ice.server.locators;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.server.SimpleOperationServant;
import pl.jdabrowa.agh.distributed.ice.util.CachedPool;

import java.util.concurrent.atomic.AtomicInteger;

public class PooledLocator extends LocatorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PooledLocator.class);

    private final CachedPool<Object> servantPool;
    private final AtomicInteger servantIndex = new AtomicInteger(0);
    private final int poolSize;

    public PooledLocator(int poolSize) {
        this.servantPool = new CachedPool<>(i -> new SimpleOperationServant());
        this.poolSize = poolSize;
    }

    @Override
    public Ice.Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        int nextServantIndex = servantIndex.getAndUpdate(i -> ((++i) % poolSize));
        LOGGER.trace("Returning pooled servant with id {}", nextServantIndex);
        return servantPool.get(nextServantIndex);
    }
}
