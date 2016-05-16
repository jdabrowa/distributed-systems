package pl.jdabrowa.distributed.jgroups.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CommandLineUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineUtil.class);

    private final Scanner scanner = new Scanner(System.in);

    public String promptAndReadLine() {
        System.out.println("Wpisz komendę:");
        System.out.print("> ");
        return scanner.nextLine();
    }

    public void printIinstructionMessage() {
        System.out.println("Wpis jedną z poniższych komend: ");
        System.out.println("\tjoin <num>\t|\tDołącza do wybranego kanału (i tworzy go, jeśli nie istnieje, 1 <= num <= 200)");
        System.out.println("\tlist\t|\tWypisuje listę dostępnych kanałów i zapisanych do nich użytkowników");
        System.out.println("\texit\t|\tKończy pracę");
    }
}
