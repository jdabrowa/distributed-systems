package pl.jdabrowa.distributed.lab2.client;

import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardFieldStatus;
import pl.jdabrowa.distributed.lab2.game.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClient extends Remote {
    Player getPlayer() throws RemoteException;
    Board getOwnBoard() throws RemoteException;
    Board getOpponentBoard() throws RemoteException;
    BoardFieldStatus tryShoot(int x, int y) throws RemoteException;
    boolean isGameFinished() throws RemoteException;
}
