package pl.jdabrowa.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationMain {

    public static void main(String ... args){
        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(ApplicationMain.class, args);
    }
}