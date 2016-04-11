package pl.jdabrowa.distributed.jms.client.tasks;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class RandomStringIdStrategy implements IdGenerationStrategy {

    public static final int ID_STRING_LENGTH = 10;

    private final AtomicLong longId = new AtomicLong(0);

    @Override
    public String generateUniqueId() {
        String randomString = generateRandomString();
        long id = longId.getAndUpdate(i -> ++i);
        return randomString + "-" + id;
    }

    private String generateRandomString() {
        return RandomStringUtils.randomAlphabetic(ID_STRING_LENGTH);
    }
}
