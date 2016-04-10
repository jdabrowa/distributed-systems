package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;

public class FirstClient extends AbstractClient {

    private static final long serialVersionUID = -1169235023056214771L;

    public FirstClient(String nick, GameImpl game) {
        super(nick, game);
    }

    @Override
    public Board getOwnBoard() {
        return asOwnBoard(game.getFirstPlayerBoard());
    }

    @Override
    public Board getOpponentBoard() {
        return asOpponentBoard(game.getSecondPlayerBoard());
    }

    @Override
    public BoardFieldStatus tryShoot(int x, int y) {
        try {
            return game.firstPlayerShoot(x, y);
        } catch (InterruptedException e) {
            e.printStackTrace(); // TODO
            return null; // TODO
        }
    }
}
