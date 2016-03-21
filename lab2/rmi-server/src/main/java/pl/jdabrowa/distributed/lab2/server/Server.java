package pl.jdabrowa.distributed.lab2.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.BoardImpl;
import pl.jdabrowa.distributed.lab2.game.Bot;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class Server extends UnicastRemoteObject implements GameServer {

    private static final long serialVersionUID = -7038858750924126231L;

    private final List<GameClient> awaitingClients;

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public Server(List<GameClient> awaitingClients) throws RemoteException {
        super();
        this.awaitingClients = awaitingClients;
    }

    @Override
    public List<Player> getFreePlayers() throws RemoteException {
//        return awaitingClients.stream().map(GameClient::getPlayer).collect(Collectors.toList());
        return null; // TODO
    }

    @Override
    public String startNewGameWithBot(Board playerBoard) throws RemoteException {
        Board botBoard = generateRandomBoard();
        Bot bot = null;
        GameClient playerClient;
        if(Math.random() > 0.5) {
            GameImpl game = new GameImpl(botBoard, playerBoard);
            GameClient botClient = new FirstClient("Bot", game);
            bot = new Bot(botClient);
            playerClient = new SecondClient("player", game);
        } else {
            GameImpl game = new GameImpl(playerBoard, botBoard);
            GameClient botClient = new SecondClient("Bot", game);
            bot = new Bot(botClient);
            playerClient = new FirstClient("player", game);
        }
        final Bot finalBot = bot;
        new Thread(() -> {
            LOGGER.info("Starting bot thread");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("Returned from sleep");
            try {
                finalBot.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();

        String identifier = RandomStringUtils.randomAlphabetic(10);
        Registry registry = LocateRegistry.getRegistry();
        GameClient stubClient = (GameClient) UnicastRemoteObject.exportObject(playerClient, 0);
        registry.rebind(identifier, stubClient);
        return identifier;
    }

    @Override
    public String startNewGameWith(Player player, Board board) throws RemoteException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String awaitOpponentAndStartGame(Board board) throws RemoteException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Board generateRandomBoard() {   // https://xkcd.com/221/
        return new BoardImpl(
                "          ",
                "  ***     ",
                "          ",
                "   *      ",
                "          ",
                "       *  ",
                "  *    *  ",
                "  *    *  ",
                "       *  ",
                " ****  *  "
        );
    }
}
