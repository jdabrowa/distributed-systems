package pl.jdabrowa.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.distributed.lab2.server.GameServer;
import pl.jdabrowa.distributed.lab2.server.Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class GameServerMain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServerMain.class);

    private void start() throws RemoteException, MalformedURLException {
        Registry registry = LocateRegistry.createRegistry(1099);
        LOGGER.info("Starting game server");
//        installSecurityManagerIfNotPresent();
        GameServer server = new Server(new ArrayList<>());
        registry.rebind("gameserver", server);
    }

    private void installSecurityManagerIfNotPresent() {
        if(null == System.getSecurityManager()) {
            LOGGER.info("No SecurityManager detected, installing new one");
            System.setSecurityManager(new SecurityManager());
            LOGGER.info("SecurityManager installed");
        }
    }

    public static void main( String[] args ) throws RemoteException, MalformedURLException {
        LOGGER.info("Codebase: {}", System.getProperty("java.rmi.server.codebase"));
        new GameServerMain().start();
    }
}
