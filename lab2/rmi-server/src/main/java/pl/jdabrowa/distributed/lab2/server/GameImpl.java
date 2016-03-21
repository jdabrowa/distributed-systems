package pl.jdabrowa.distributed.lab2.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

import java.io.Serializable;
import java.util.concurrent.Semaphore;

public class GameImpl implements Game, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameImpl.class);

    private static final long serialVersionUID = -2583587729403829978L;

    private final Semaphore firstPlayerSemaphore;
    private final Semaphore secondPlayerSemaphore;

    private final Board firstPlayerBoard;
    private final Board secondPlayerBoard;

    public GameImpl(Board firstPlayerBoard, Board secondPlayerBoard) {
        this.firstPlayerBoard = firstPlayerBoard;
        this.secondPlayerBoard = secondPlayerBoard;
        this.firstPlayerSemaphore = new Semaphore(1, true);
        this.secondPlayerSemaphore = new Semaphore(0, true);
    }

    BoardFieldStatus firstPlayerShoot(int x, int y) throws InterruptedException {
        LOGGER.info("On first player move");
        LOGGER.info("First player semaphore: {}, second: {}", firstPlayerSemaphore.availablePermits(), secondPlayerSemaphore.availablePermits());
        return semaphoreSecuredShoot(x, y, firstPlayerSemaphore, secondPlayerSemaphore, secondPlayerBoard);
    }

    BoardFieldStatus secondPlayerShoot(int x, int y) throws InterruptedException {
        LOGGER.info("On second player move");
        LOGGER.info("First player semaphore: {}, second: {}", firstPlayerSemaphore.availablePermits(), secondPlayerSemaphore.availablePermits());
        return semaphoreSecuredShoot(x, y, secondPlayerSemaphore, firstPlayerSemaphore, firstPlayerBoard);
    }

    private BoardFieldStatus semaphoreSecuredShoot(int x, int y, Semaphore acquireSemaphore, Semaphore releaseSemaphore, Board board)
            throws InterruptedException {
        acquireSemaphore.acquire();
        try {
            return shoot(board, x, y);
        } finally {
            releaseSemaphore.release();
        }
    }

    private BoardFieldStatus shoot(Board playerBoard, int x, int y) {
        BoardFieldStatus status = playerBoard.getFiledStatus(x, y);
        if(BoardFieldStatus.SHIP == status || BoardFieldStatus.HIT == status) {
            playerBoard.setStatus(x, y, BoardFieldStatus.HIT);
            return BoardFieldStatus.HIT;
        } else {
            playerBoard.setStatus(x, y, BoardFieldStatus.MISS);
            return BoardFieldStatus.MISS;
        }
    }

    @Override
    public Board getFirstPlayerBoard() {
        return firstPlayerBoard;
    }

    @Override
    public Board getSecondPlayerBoard() {
        return secondPlayerBoard;
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
