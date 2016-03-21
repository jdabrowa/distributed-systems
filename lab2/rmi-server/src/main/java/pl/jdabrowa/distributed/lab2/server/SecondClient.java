package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;

public class SecondClient extends AbstractClient {

    private static final long serialVersionUID = -5580137841249662288L;

    public SecondClient(String nick, GameImpl game) {
        super(nick, game);
    }

    @Override
    public Board getOwnBoard() {
        return asOwnBoard(game.getSecondPlayerBoard());
    }

    @Override
    public Board getOpponentBoard() {
        return asOpponentBoard(game.getFirstPlayerBoard());
    }

    @Override
    public BoardFieldStatus tryShoot(int x, int y) {
        try {
            return game.secondPlayerShoot(x, y);
        } catch (InterruptedException e) {
            e.printStackTrace(); // TODO
            return null; // TODO
        }
    }
}
