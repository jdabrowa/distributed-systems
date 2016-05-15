package pl.jdabrowa.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jms.client.Service;
import pl.jdabrowa.distributed.jms.client.ServiceFactory;

import java.util.Scanner;

@Component
public class ApplicationMain implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);
    private final ServiceFactory serviceFactory;

    @Autowired
    public ApplicationMain(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.info("Starting main application...");
        Service service = serviceFactory.createService(null);
        Scanner scanner = new Scanner(System.in);
        while(true){
            LOGGER.info("Enter message");
            String msg = scanner.nextLine();
            byte[] bytes = service.sendAndReceive(msg.getBytes(), 1000);
            LOGGER.info("Received response: " + new String(bytes));
        }
    }
}
