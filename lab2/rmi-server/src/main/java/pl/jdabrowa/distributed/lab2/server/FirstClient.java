package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

public class FirstClient implements GameClient {

    private final Player player;
    private final GameImpl game;

    public FirstClient(String nick, GameImpl game) {
        this.player = () -> nick;
        this.game = game;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public BoardFieldStatus tryShoot(int x, int y) {
        return game.;
    }
}
