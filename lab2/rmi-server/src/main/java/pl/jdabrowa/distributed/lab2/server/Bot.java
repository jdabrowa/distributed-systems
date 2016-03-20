package pl.jdabrowa.distributed.lab2.server;

import org.apache.commons.lang3.tuple.Pair;
import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

import java.util.Random;

public class Bot implements GameClient{

    private final Player player;
    private final Game game;
    private final Random random = new Random();

    public Bot(String nick, Game game) {
        this.player = () -> nick;
        this.game = game;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void handleServerMessage(String message) {

    }

    @Override
    public void onOpponentMove() {

    }

    public void start() {
        while(!game.isFinished()) {
            Pair<Integer, Integer> nextHit = chooseRandomCoordsOfFreeField();
            game.tryShoot(nextHit.getLeft(), nextHit.getRight());
        }
    }

    private Pair<Integer, Integer> chooseRandomCoordsOfFreeField() {
        int x, y;
        int boardWidth = game.getOpponentBoard().getWidth();
        int boardHeight = game.getOpponentBoard().getHeight();
        do {
            x = random.nextInt(boardWidth);
            y = random.nextInt(boardHeight);
        } while(BoardFieldStatus.UNKNOWN != game.getOpponentBoard().getFiledStatus(x, y));
        return Pair.of(x, y);
    }
}
