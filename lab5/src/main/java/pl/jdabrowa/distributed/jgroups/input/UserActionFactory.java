package pl.jdabrowa.distributed.jgroups.input;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jgroups.IChat;
import pl.jdabrowa.distributed.jgroups.input.error.InputException;

@Component
public class UserActionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActionFactory.class);
    private static final UserAction EMPTY_ACTION = () -> {};

    private final IChat chat;

    @Autowired
    public UserActionFactory(IChat chat) {
        this.chat = chat;
    }

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
            return new JoinChannelAction(extractAfter(line, UserInstructions.JOIN_INSTRUCTION));
        } else if(line.startsWith(UserInstructions.SEND_INSTRUCTION)) {
            return new SendAction(extractAfter(line, UserInstructions.SEND_INSTRUCTION));
        } else {
            LOGGER.warn("Unknown action, type 'help' to list available commands");
            return EMPTY_ACTION;
        }
    }

    private String extractAfter(String line, String prefix) {
        return line.substring(prefix.length());
    }

    private final class JoinChannelAction implements UserAction {

        private final String channelName;

        private JoinChannelAction(String channelName) {
            validateChannelNameAsNumber(channelName);
            this.channelName = channelName;
        }

        private void validateChannelNameAsNumber(String channelName) {
            if(StringUtils.isEmpty(channelName) || !StringUtils.isNumeric(channelName) || !isInValidRange(Integer.valueOf(channelName))) {
                throw new InputException("Channel number must be integer number from range [1;200]");
            }
        }

        @Override
        public void execute() {
            try {
                chat.joinChannel(channelName);
            } catch (Exception e) {
                LOGGER.warn("Cannot join channel", e);
            }
        }
    }

    private final class SendAction implements UserAction {
        private final String text;

        private SendAction(String text) {
            this.text = text;
        }

        @Override
        public void execute() {
            try {
                chat.sendMessage(text);
            } catch (Exception e) {
                LOGGER.warn("Cannot send message", e);
            }
        }
    }

    private boolean isInValidRange(int number) {
        return number > 0 && number <= 200;
    }
}
