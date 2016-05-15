package pl.jdabrowa.distributed.jgroups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jgroups.configuration.ChatClientConfiguration;
import pl.jdabrowa.distributed.jgroups.input.CommandLineUtil;

@Component
public class ChatClient implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClient.class);

    private final ChatClientConfiguration configuration;
    private final CommandLineUtil cmd;

    @Autowired
    public ChatClient(ChatClientConfiguration configuration, CommandLineUtil cmd) {
        this.configuration = configuration;
        this.cmd = cmd;
    }

    @Override
    public void run(String ... strings) throws Exception {
        cmd.printIinstructionMessage();
        boolean shouldContinue = true;
        while(shouldContinue) {
            String line = cmd.promptAndReadLine();
        }
    }
}
