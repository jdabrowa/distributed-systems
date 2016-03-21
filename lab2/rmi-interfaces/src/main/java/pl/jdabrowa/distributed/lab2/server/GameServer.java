package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.Board;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameServer extends Remote {
    List<Player> getFreePlayers() throws RemoteException;
    String startNewGameWithBot(Board board) throws RemoteException;
    String startNewGameWith(Player player, Board board) throws RemoteException;
    String awaitOpponentAndStartGame(Board board) throws RemoteException;
}
