package pl.jdabrowa.agh.distributed.ice.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.server.locators.util.ServantRepository;

import java.util.Date;

public class MockRepository implements ServantRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockRepository.class);

    @Override
    public Ice.Object getServant(String ID) {
        LOGGER.debug("Restoring persisted Servant...");
        Date cacheDate = new Date(1461531529287L);
        return new SimpleOperationServant(cacheDate, 0);
    }
}
