package pl.jdabrowa.distributed.lab2.client.configuration;

import org.apache.commons.lang3.StringUtils;
import pl.jdabrowa.distributed.lab2.client.exception.ExceptionMessages;
import pl.jdabrowa.distributed.lab2.client.exception.GameConfigurationException;

import java.util.Arrays;
import java.util.List;

public class ConfigurationValidator {

    private static final List<String> AVAILABLE_GAME_TYPES = Arrays.asList("PLAYER", "BOT");

    public void validateParams(String[] args) throws GameConfigurationException {
        if(2 != args.length) {
            throw new GameConfigurationException(ExceptionMessages.INCORRECT_NUMBER_OF_ARGUMENTS);
        }

        if(StringUtils.isEmpty(args[0])) {
            throw new GameConfigurationException(ExceptionMessages.NICK_MUST_NOT_BE_EMPTY);
        }

        if(StringUtils.isEmpty(args[1])) {
            throw new GameConfigurationException(ExceptionMessages.TYPE_MUST_NOT_BE_EMPTY);
        }

        if(!AVAILABLE_GAME_TYPES.contains(args[1])) {
            throw new GameConfigurationException(ExceptionMessages.TYPE_MUST_BE_CORRECT);
        }
    }
}
