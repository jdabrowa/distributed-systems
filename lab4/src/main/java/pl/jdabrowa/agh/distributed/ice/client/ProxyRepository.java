package pl.jdabrowa.agh.distributed.ice.client;

import Ice.ObjectPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.error.IceError;
import pl.jdabrowa.agh.distributed.ice.util.CachedPool;

public class ProxyRepository<T extends ObjectPrx> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyRepository.class);
    private static final String ICE_CLIENT_PROPERTY_KEY_TEMPLATE = "pl.jdabrowa.agh.distributed.ice.client.SimpleClient.Proxy-K";

    private final IceEnvironment ice;
    private final CachedPool<T> proxyPool;
    private final ProxyCastOperation<T> castOperation;

    public ProxyRepository(IceEnvironment ice, ProxyCastOperation<T> castOperation) {
        this.ice = ice;
        this.castOperation = castOperation;
        this.proxyPool = new CachedPool<>(this::initializeAndCacheProxy);
    }

    public T getProxyForCategory(Integer categoryId) throws IceError {
        return proxyPool.get(categoryId);
    }

    private T initializeAndCacheProxy(Integer categoryId) {
        String proxyIdWithCategory = ICE_CLIENT_PROPERTY_KEY_TEMPLATE + categoryId;
        ObjectPrx proxyForCategory = initializeProxyUsingKey(proxyIdWithCategory);
        T castedProxy = castOperation.cast(proxyForCategory);
        validateProxyNotNull(proxyForCategory, proxyIdWithCategory);
        return castedProxy;
    }

    private ObjectPrx initializeProxyUsingKey(String proxyIdWithCategory) {
        LOGGER.debug("Creating proxy with key {}", proxyIdWithCategory);
        return ice.getCommunicator().propertyToProxy(proxyIdWithCategory);
    }

    private void validateProxyNotNull(ObjectPrx proxyForCategory, String proxyIdWithCategory) {
        if(null == proxyForCategory) {
            LOGGER.warn("Proxy instantiation error - proxy defined by property '{}' is null", proxyIdWithCategory);
        }
    }

    public interface ProxyCastOperation<T> {
        T cast(ObjectPrx object);
    }
}
