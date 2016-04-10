package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.OpponentBoard;

import java.io.Serializable;

public abstract class AbstractClient implements GameClient, Serializable {

    private static final long serialVersionUID = 7010450869806564307L;

    protected final Player player;
    protected final GameImpl game;

    public AbstractClient(String nick, GameImpl game) {
        this.player = () -> nick;
        this.game = game;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isGameFinished() {
        return game.isFinished();
    }

    protected Board asOwnBoard(Board board) {
        return board;
    }

    protected Board asOpponentBoard(Board board) {
        return new OpponentBoard(board);
    }
}
