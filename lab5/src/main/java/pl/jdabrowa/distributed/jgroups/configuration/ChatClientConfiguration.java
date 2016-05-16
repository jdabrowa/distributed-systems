package pl.jdabrowa.distributed.jgroups.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatClientConfiguration {

    @Value("${chat.client.nickname}")
    private String userName;

}
