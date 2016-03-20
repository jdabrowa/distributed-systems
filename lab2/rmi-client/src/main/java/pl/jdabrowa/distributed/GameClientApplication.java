package pl.jdabrowa.distributed;

import pl.jdabrowa.distributed.lab2.client.BoardImpl;
import pl.jdabrowa.distributed.lab2.client.GameClient;
import pl.jdabrowa.distributed.lab2.client.Player;
import pl.jdabrowa.distributed.lab2.client.configuration.ConfigurationValidator;
import pl.jdabrowa.distributed.lab2.client.exception.ExceptionMessages;
import pl.jdabrowa.distributed.lab2.client.exception.GameConfigurationException;
import pl.jdabrowa.distributed.lab2.game.Board;
import pl.jdabrowa.distributed.lab2.game.Game;
import pl.jdabrowa.distributed.lab2.game.GameType;
import pl.jdabrowa.distributed.lab2.server.GameServer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class GameClientApplication {


    private GameClientApplication() {

    }

    private void start(String ... args) throws GameConfigurationException, RemoteException {

        GameServer server = null;
        new ConfigurationValidator().validateParams(args);

        GameType gameType = GameType.valueOf(args[1]);
        switch(gameType) {
            case WITH_OTHER_PLAYER:
                startGameWithOtherPlayer(server);
                break;
            case WITH_BOT:
                startGameWithBot(server);
                break;
            default:    // should not happen after validation
                throw new GameConfigurationException(ExceptionMessages.TYPE_MUST_BE_CORRECT);
        }
    }

    private void startGameWithBot(GameServer server) throws RemoteException {
        Board board = generateRandomBoard();
        GameClient client = server.startNewGameWithBot();
        enterGameLoop(client);
    }

    private Board generateRandomBoard() {
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

    private void enterGameLoop(GameClient client) {
        int x, y;
        Scanner scanner = new Scanner(System.in);

        Game game = client.getGame();

        while(!game.isFinished()) {
            printBoard(game);
            System.out.println("Enter next shoot coords in form: x y");
            x = scanner.nextInt();
            y = scanner.nextInt();
            client.tryShoot(x, y);
        }
    }

    private void printBoard(Game game) {

        Board yourBoard = game.getYourBoard();
        Board opponentBoard = game.getOpponentBoard();

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

    private void startGameWithOtherPlayer(GameServer server) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available players: ");
        List<Player> freePlayers = server.getFreePlayers();
        listPlayers(freePlayers);
        String playerNickToPlayWith = scanner.nextLine();
        while(!freePlayers.contains(playerNickToPlayWith)) {
            System.out.println("Player " + playerNickToPlayWith + " is not available, please choose one of available players:");
            freePlayers = server.getFreePlayers();
            listPlayers(freePlayers);
            playerNickToPlayWith = scanner.nextLine();
        }
        Player player = null;
        for(Player freePlayer : freePlayers) {
            if(playerNickToPlayWith.equals(freePlayer.getNickName())) {
                player = freePlayer;
            }
        }
        NewGame newGame = server.startNewGameWith(player);
        Game game = newGame.forClient(client);

        enterGameLoop(game);
    }

    private void listPlayers(List<Player> freePlayers) {
        for(Player player : freePlayers) {
            System.out.println("\t" + player.getNickName());
        }
    }



    public static void main(String ... args) throws GameConfigurationException, RemoteException {
        new GameClientApplication().start(args);
    }
}
