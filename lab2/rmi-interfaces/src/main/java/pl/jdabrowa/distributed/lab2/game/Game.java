package pl.jdabrowa.distributed.lab2.game;

public interface Game {
    void end();
    boolean isFinished();
    Board getYourBoard();
    Board getOpponentBoard();
}
