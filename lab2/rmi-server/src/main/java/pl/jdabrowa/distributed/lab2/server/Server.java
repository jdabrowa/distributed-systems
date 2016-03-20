package pl.jdabrowa.distributed.lab2.server;

import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.NewGame;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class Server implements GameServer {

    private final List<GameClient> awaitingClients;

    public Server(List<GameClient> awaitingClients) {
        this.awaitingClients = awaitingClients;
    }

    @Override
    public List<Player> getFreePlayers() throws RemoteException {
        return awaitingClients.stream().map(GameClient::getPlayer).collect(Collectors.toList());
    }

    @Override
    public NewGame startNewGameWithBot() throws RemoteException {
        return null;
    }

    @Override
    public NewGame startNewGameWith(Player player) throws RemoteException {
        return null;
    }

    @Override
    public NewGame awaitOpponentAndStartGame() throws RemoteException {
        return null;
    }
}
