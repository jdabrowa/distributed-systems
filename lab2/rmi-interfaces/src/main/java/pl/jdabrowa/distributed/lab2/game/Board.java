package pl.jdabrowa.distributed.lab2.game;

public interface Board {

    int getWidth();
    int getHeight();

    BoardFieldStatus getFiledStatus(int x, int y);
}
