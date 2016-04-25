package pl.jdabrowa.agh.distributed.ice.server.evictor;

import Ice.Current;
import Ice.LocalObjectHolder;

public abstract class EvictorBase implements Ice.ServantLocator {

    public static final int DEFAULT_SIZE = 5;

    private int size;

    public EvictorBase() {
        size = DEFAULT_SIZE;
    }

    public EvictorBase(int size) {
        this.size = size < 0 ? DEFAULT_SIZE : size;
    }

    public abstract Ice.Object add(Current c, LocalObjectHolder cookie);

    public abstract void evict(Ice.Object servant, java.lang.Object cookie);

    synchronized public final Ice.Object locate(Current c, LocalObjectHolder cookie) {
        return null;
    }

    synchronized public final void finished(Ice.Current c, Ice.Object o, java.lang.Object cookie) {

    }

    synchronized public final void deactivate(String category) {

    }
}
