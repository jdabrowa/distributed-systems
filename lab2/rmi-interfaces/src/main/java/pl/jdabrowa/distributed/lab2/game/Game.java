package pl.jdabrowa.distributed.lab2.game;

public interface Game {
    void end();
    boolean isFinished();
    Board getFirstPlayerBoard();
    Board getSecondPlayerBoard();
}
