package pl.jdabrowa.distributed.jgroups.input;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jgroups.input.error.InputException;

@Component
public class UserActionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActionFactory.class);
    private static final UserAction EMPTY_ACTION = () -> {};

    public UserAction createFromLine(String line) {
        line = line.toLowerCase();
        try {
            return dispatch(line);
        } catch (Exception e) {
            LOGGER.warn("Cannot execute action", e);
            return EMPTY_ACTION;
        }
    }

    private UserAction dispatch(String line) {
        if(line.startsWith(UserInstructions.JOIN_INSTRUCTION)) {
            return new JoinChannelAction(line.substring(UserInstructions.JOIN_INSTRUCTION.length()));
        }
    }

    private final class JoinChannelAction implements UserAction {

        private JoinChannelAction(String channelName) {
            validateChannelNameAsNumber(channelName);
        }

        private void validateChannelNameAsNumber(String channelName) {
            if(StringUtils.isEmpty(channelName) || !StringUtils.isNumeric(channelName) || !isInValidRange(Integer.valueOf(channelName))) {
                throw new InputException("Channel number must be integer number from range [1;200]");
            }
        }

        @Override
        public void execute() {

        }
    }

    private boolean isInValidRange(int number) {
        return number > 0 && number <= 200;
    }
}
