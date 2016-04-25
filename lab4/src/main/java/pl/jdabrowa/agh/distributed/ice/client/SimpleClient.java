package pl.jdabrowa.agh.distributed.ice.client;

import Ice.Communicator;
import Ice.ObjectPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.error.IceError;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1.SimpleStringOperationPrx;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1.SimpleStringOperationPrxHelper;

import java.util.Scanner;

public class SimpleClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClient.class);

    private static final String ICE_CLIENT_PROPERTY_KEY = "pl.jdabrowa.agh.distributed.ice.client.SimpleClient.Proxy";

    private final SimpleStringOperationPrx simpleStringOperationProxy;

    public SimpleClient(String ... args) throws IceError {
        LOGGER.info("Initializing ICE");
        Communicator communicator = Ice.Util.initialize(args);
        LOGGER.info("ICE initialized");
        LOGGER.info("Creating proxy for Simple String operations...");
        ObjectPrx rawSimpleStringOperationProxy = communicator.propertyToProxy(ICE_CLIENT_PROPERTY_KEY);
        this.simpleStringOperationProxy = SimpleStringOperationPrxHelper.checkedCast(rawSimpleStringOperationProxy);
        if(null == simpleStringOperationProxy) {
            throw new IceError("Proxy instantiation error - proxy defined by property '" + ICE_CLIENT_PROPERTY_KEY + "' is null");
        }
        LOGGER.info("Proxy created");
    }


    private void enterProcessingLoop() {

        LOGGER.info("Entering processing loop");

        boolean shouldContinue = true;
        Scanner systemInScanner = new Scanner(System.in);
        while(shouldContinue) {
            LOGGER.info("Enter message to process:");
            String inputLine = systemInScanner.nextLine();
            shouldContinue = processLine(inputLine);
        }
    }

    private boolean processLine(String inputLine) {
        boolean shouldContinue = true;
        if("exit".equals(inputLine)) {
            shouldContinue = false;
        } else {
            String result = simpleStringOperationProxy.invoke(inputLine);
            System.out.println(result);
        }
        return shouldContinue;
    }

    public static void main(String[] args) throws IceError {
        LOGGER.info("Initializing client application...");
        new SimpleClient(args).enterProcessingLoop();
    }
}
