package pl.jdabrowa.agh.distributed.ice.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedPool<T> {

    private final Map<Integer, T> cache = new ConcurrentHashMap<>();
    private final Initializer<T> initializer;

    public CachedPool(Initializer<T> initializer) {
        this.initializer = initializer;
    }

    public T get(int id) {
        T cachedItem = cache.get(id);
        if(null == cachedItem) {
            cachedItem = initializeAndCacheThreadsafe(id);
        }
        return cachedItem;
    }

    private T initializeAndCacheThreadsafe(Integer id) {
        T cachedItem;
        synchronized (cache) {
            cachedItem = cache.get(id);
            if(null == cachedItem) {
                cachedItem = initializeAndCache(id);
            }
        }
        return cachedItem;
    }

    private T initializeAndCache(Integer id) {
        T item = initializer.initialize(id);
        cache.put(id, item);
        return item;
    }

    public interface Initializer<T> {
        T initialize(int id);
    }
}
