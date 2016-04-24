package pl.jdabrowa.agh.distributed.ice.client;

import Ice.Communicator;
import Ice.ObjectPrx;
import pl.jdabrowa.agh.distributed.ice.error.IceError;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1.SimpleStringOperationPrx;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1.SimpleStringOperationPrxHelper;

import java.util.Scanner;

public class SimpleClient {

    private static final String ICE_CLIENT_PROPERTY_KEY = "pl.jdabrowa.agh.distributed.ice.client.SimpleClient.Proxy";

    private final SimpleStringOperationPrx simpleStringOperationProxy;

    public SimpleClient(String ... args) throws IceError {
        Communicator communicator = Ice.Util.initialize(args);
        ObjectPrx rawSimpleStringOperationProxy = communicator.propertyToProxy(ICE_CLIENT_PROPERTY_KEY);
        this.simpleStringOperationProxy = SimpleStringOperationPrxHelper.checkedCast(rawSimpleStringOperationProxy);
        if(null == simpleStringOperationProxy) {
            throw new IceError("Proxy instantiation error - proxy defined by property '" + ICE_CLIENT_PROPERTY_KEY + "' is null");
        }
    }


    private void enterProcessingLoop() {

        boolean shouldContinue = true;
        Scanner systemInScanner = new Scanner(System.in);
        while(shouldContinue) {
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
        new SimpleClient(args).enterProcessingLoop();
    }
}
