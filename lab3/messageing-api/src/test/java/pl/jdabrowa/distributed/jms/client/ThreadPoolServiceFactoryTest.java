package pl.jdabrowa.distributed.jms.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@TestPropertySource(properties = {"messaging.pool.size:1"})
public class ThreadPoolServiceFactoryTest {

    @Autowired
    private ServiceFactory instance;

    @Test
    public void shouldInitializeStuff() {

        // given

        // when

        // then

    }

}