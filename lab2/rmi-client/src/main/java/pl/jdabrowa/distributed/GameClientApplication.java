package pl.jdabrowa.distributed;

import pl.jdabrowa.distributed.lab2.game.BoardImpl;
import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.client.configuration.ConfigurationValidator;
import pl.jdabrowa.distributed.lab2.client.exception.ExceptionMessages;
import pl.jdabrowa.distributed.lab2.client.exception.GameConfigurationException;
import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.GameType;
import pl.jdabrowa.distributed.lab2.server.GameServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class GameClientApplication {

    private String IP = "164.132.230.106";

    private GameClientApplication() {

    }

    private void start(String ... args) throws GameConfigurationException, RemoteException, MalformedURLException, NotBoundException {

        new ConfigurationValidator().validateParams(args);
        IP = args[0];

        GameServer server = (GameServer) Naming.lookup("rmi://" + IP + ":1099/gameserver");

        GameType gameType = GameType.valueOf(args[1]);
        switch(gameType) {
            case PLAYER:
                startGameWithOtherPlayer(server);
                break;
            case BOT:
                startGameWithBot(server);
                break;
            default:    // should not happen after validation
                throw new GameConfigurationException(ExceptionMessages.TYPE_MUST_BE_CORRECT);
        }
    }

    private void startGameWithBot(GameServer server) throws RemoteException, MalformedURLException, NotBoundException {
        Board board = generateRandomBoard();
        String identifier = server.startNewGameWithBot(board);
        GameClient client = (GameClient) Naming.lookup("rmi://" + IP + ":1099/" + identifier);
        enterGameLoop(client);
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

    private void enterGameLoop(GameClient client) throws RemoteException {

        int x, y;
        Scanner scanner = new Scanner(System.in);


        while(!client.isGameFinished()) {
            printBoards(client);
            System.out.println("Enter next shoot coords in form: x y");
            x = scanner.nextInt();
            y = scanner.nextInt();
            client.tryShoot(x, y);
        }
    }

    private void printBoards(GameClient client) throws RemoteException {

        Board yourBoard = client.getOwnBoard();
        Board opponentBoard = client.getOpponentBoard();

        for(int row = 0; row < yourBoard.getHeight(); ++row) {
            for(int col = 0; col < yourBoard.getWidth(); ++col) {
                System.out.print(yourBoard.getFiledStatus(col, row).getMark());
            }

            System.out.print("  |  ");

            for(int col = 0; col < opponentBoard.getWidth(); ++col) {
                System.out.print(opponentBoard.getFiledStatus(col, row).getMark());
            }
            System.out.println();
        }
    }

    private void startGameWithOtherPlayer(GameServer server) throws RemoteException, MalformedURLException, NotBoundException {

        String identifier = server.awaitOpponentAndStartGame(generateRandomBoard());
        GameClient client = (GameClient) Naming.lookup("rmi://" + IP + ":1099/" + identifier);

        enterGameLoop(client);
    }

    private void listPlayers(List<Player> freePlayers) {
        for(Player player : freePlayers) {
            System.out.println("\t" + player.getNickName());
        }
    }



    public static void main(String ... args) throws GameConfigurationException, RemoteException, MalformedURLException, NotBoundException {
        new GameClientApplication().start(args);
    }
}
