package net.emullen;

import net.emullen.Connectionless.ConnectionlessClient;
import net.emullen.Connectionless.ConnectionlessServer;
import net.emullen.Socketed.Client;
import net.emullen.Socketed.Server;

public class Main {
    public static void main(String[] args) {
        ProgArgs progArgs = new ProgArgs(args);

        if(progArgs.isServer()) {
            if(progArgs.isConnectionless())
                new ConnectionlessServer(progArgs);
            else
                new Server(progArgs);
        } else {
            if(progArgs.isConnectionless())
                new ConnectionlessClient(progArgs);
            else
                new Client(progArgs);
        }
    }
}
