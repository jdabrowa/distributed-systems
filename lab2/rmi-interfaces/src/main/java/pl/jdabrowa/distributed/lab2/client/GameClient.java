package pl.jdabrowa.distributed.lab2.client;

import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

public interface GameClient {
    Player getPlayer();
    Game getGame();
    BoardFieldStatus tryShoot(int x, int y);
}
