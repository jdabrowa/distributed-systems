package pl.jdabrowa.agh.distributed.ice.client;

import Ice.Communicator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.util.IceConfigurator;

public class IceEnvironment {

    private static final Logger LOGGER = LoggerFactory.getLogger(IceEnvironment.class);

    @Getter
    private final Communicator communicator;

    public IceEnvironment(String configurationFileName, String ... args) {
        LOGGER.info("Initializing ICE...");
        String [] iceArgs = new IceConfigurator().addConfiguration(args, configurationFileName);
        this.communicator = Ice.Util.initialize(iceArgs);
        LOGGER.info("ICE initialized");
    }
}
