package pl.jdabrowa.distributed.lab2.client;

import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;

public class BoardImpl implements Board {

    private final BoardFieldStatus [][] internalBoard;
    private final int width;
    private final int height;

    public BoardImpl(String ... rows) {
        validateEqualRows(rows);
        this.width = rows[0].length();
        this.height = rows.length;
        internalBoard = new BoardFieldStatus[height][width];
        translateRows(rows);
    }

    private void translateRows(String ... rows) {
        for(int y = 0; y < rows.length; ++y) {
            for(int x = 0; x < rows[y].length(); ++x) {
                internalBoard[y][x] = BoardFieldStatus.valueOf(String.valueOf(rows[y].charAt(x)));
            }
        }
    }

    private void validateEqualRows(String[] rows) {
        int firstRowLenth = rows[0].length();
        for(String row : rows) {
            if(row.length() != firstRowLenth) {
                throw new IllegalArgumentException("Rows should have equal length");
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public BoardFieldStatus getFiledStatus(int x, int y) {
        return internalBoard[y][x];
    }
}
