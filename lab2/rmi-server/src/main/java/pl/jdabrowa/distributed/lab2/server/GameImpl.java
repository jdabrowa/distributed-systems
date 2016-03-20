package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

import java.util.concurrent.Semaphore;

public class GameImpl implements Game {

    private final Semaphore firstPlayerSemaphore;
    private final Semaphore secondPlayerSemaphore;

    private final Board firstPlayerBoard;
    private final Board secondPlayerBoard;

    public GameImpl(GameClient firstPlayer, GameClient secondPlayer) {
        this.firstPlayerBoard = firstPlayer;
        this.secondPlayerBoard = secondPlayer;
        this.firstPlayerSemaphore = new Semaphore(1);
        this.secondPlayerSemaphore = new Semaphore(0);
    }

    BoardFieldStatus firstPlayerShoot(int x, int y) throws InterruptedException {
        firstPlayerSemaphore.acquire();
        try {
            return
        }
    }

    BoardFieldStatus secondPlayerShoot(int x, int y) {

    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Board getYourBoard() {
        return null;
    }

    @Override
    public Board getOpponentBoard() {
        return null;
    }
}
