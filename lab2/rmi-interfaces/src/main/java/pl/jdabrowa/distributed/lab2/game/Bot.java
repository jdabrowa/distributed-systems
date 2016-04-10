package pl.jdabrowa.distributed.lab2.game;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

import java.rmi.RemoteException;
import java.util.Random;

public class Bot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private final Random random = new Random();
    private final GameClient client;

    public Bot(GameClient client) {
        this.client = client;
    }

    public void start() throws RemoteException {
        LOGGER.info("Starting bot action...");
        while(!client.isGameFinished()) {
            Pair<Integer, Integer> nextHit = chooseRandomCoordsOfFreeField();
            LOGGER.info("Bot tries shoot: {}", nextHit);
            client.tryShoot(nextHit.getLeft(), nextHit.getRight());
        }
    }

    private Pair<Integer, Integer> chooseRandomCoordsOfFreeField() throws RemoteException {
        int x, y;
        int boardWidth = client.getOpponentBoard().getWidth();
        int boardHeight = client.getOpponentBoard().getHeight();
        do {
            LOGGER.debug("Losuje");
            x = random.nextInt(boardWidth);
            y = random.nextInt(boardHeight);
        } while(BoardFieldStatus.UNKNOWN != client.getOpponentBoard().getFiledStatus(x, y));
        return Pair.of(x, y);
    }
}
