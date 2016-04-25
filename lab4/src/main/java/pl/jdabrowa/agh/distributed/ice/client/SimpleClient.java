package pl.jdabrowa.agh.distributed.ice.client;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.error.IceError;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1.SimpleStringOperationPrx;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1.SimpleStringOperationPrxHelper;

import java.util.Scanner;

public class SimpleClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClient.class);

    private static final String CONFIGURATION_FILE_NAME = "ice-client.properties";
    private static final ProxyRepository.ProxyCastOperation<SimpleStringOperationPrx> TO_SIMPLE_STRING_PROXY = SimpleStringOperationPrxHelper::checkedCast;

    private final ProxyRepository<SimpleStringOperationPrx> proxyRepository;

    public SimpleClient(String ... args) throws IceError {

        this.proxyRepository = new ProxyRepository<>(new IceEnvironment(CONFIGURATION_FILE_NAME, args), TO_SIMPLE_STRING_PROXY);
    }


    private void enterProcessingLoop() {

        LOGGER.info("Entering processing loop");

        boolean shouldContinue = true;
        Scanner systemInScanner = new Scanner(System.in);
        while(shouldContinue) {
            LOGGER.info("Enter message of form '<category_id> <message>' to process:");
            System.out.print("> ");
            String inputLine = systemInScanner.nextLine();
            shouldContinue = processLine(inputLine);
        }
    }

    private boolean processLine(String inputLine) {
        boolean shouldContinue = true;
        if("exit".equals(inputLine)) {
            shouldContinue = false;
        } else {
            String [] inputGroups = inputLine.split("\\s");
            if(2 != inputGroups.length || !StringUtils.isNumeric(inputGroups[0])) {
                LOGGER.warn("Incorrect message format");
            } else {
                int categoryId = Integer.valueOf(inputGroups[0]);
                String message = inputGroups[1];
                String result = null;
                try {
                    result = proxyRepository.getProxyForCategory(categoryId).invoke(message);
                } catch (IceError iceError) {
                    LOGGER.warn("ICE error:", iceError);
                }
                System.out.println(result);
            }

        }
        return shouldContinue;
    }

    public static void main(String[] args) throws IceError {
        LOGGER.info("Initializing client application...");
        new SimpleClient(args).enterProcessingLoop();
    }
}
