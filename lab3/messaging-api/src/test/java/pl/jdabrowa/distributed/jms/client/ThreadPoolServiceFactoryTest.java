package pl.jdabrowa.distributed.jms.client;

import org.apache.activemq.broker.BrokerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.jdabrowa.distributed.jms.client.error.MessagingException;
import pl.jdabrowa.distributed.jms.server.ServerSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Main.class, ServerSetup.class})
@Profile("test")
@TestPropertySource(locations = {"classpath:application-test.properties"}, properties = "logging.level:INFO")
public class ThreadPoolServiceFactoryTest {

    @Autowired
    private ServiceFactory instance;

    @Autowired
    BrokerService service;


    @Test(expected = MessagingException.class)
    public void requestShouldTimeOut() throws MessagingException {

        // given

        // when
        instance.createService(null).sendAndReceive("dupa".getBytes(), 1000);

        // then

    }

}