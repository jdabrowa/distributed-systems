package pl.jdabrowa.distributed.lab2.game;

import java.io.Serializable;

public class OpponentBoard implements Board, Serializable {

    private static final long serialVersionUID = 3137746215822402727L;

    private final Board originalBoard;

    public OpponentBoard(Board board) {
        this.originalBoard = board;
    }

    @Override
    public int getWidth() {
        return originalBoard.getWidth();
    }

    @Override
    public int getHeight() {
        return originalBoard.getHeight();
    }

    @Override
    public BoardFieldStatus getFiledStatus(int x, int y) {
        switch (originalBoard.getFiledStatus(x, y)) {
            case HIT:
                return BoardFieldStatus.HIT;
            case MISS:
                return BoardFieldStatus.MISS;
            default:
                return BoardFieldStatus.UNKNOWN;
        }
    }

    @Override
    public void setStatus(int x, int y, BoardFieldStatus newStatus) {
        throw new UnsupportedOperationException("You cannot modify your opponent's board");
    }
}
