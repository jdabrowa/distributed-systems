package pl.jdabrowa.distributed.lab2.client.exception;

public class ExceptionMessages {
    public static final String INCORRECT_NUMBER_OF_ARGUMENTS = "Two arguments expected: hostname and game type (PLAYER or BOT)";
    public static final String NICK_MUST_NOT_BE_EMPTY = "First program argument (hostname) must not be empty";
    public static final String TYPE_MUST_NOT_BE_EMPTY = "Second program argument (game type) must not be empty";
    public static final String TYPE_MUST_BE_CORRECT = "Second program argument (game type) must be either PLAYER or BOT";
}
