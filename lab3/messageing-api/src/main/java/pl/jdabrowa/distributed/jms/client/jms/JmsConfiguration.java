package pl.jdabrowa.distributed.jms.client.jms;

import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "jms.configuration")
public class JmsConfiguration {

    @Setter @Getter
    private Map<String, String> jmsProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
}
