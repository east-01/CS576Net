package net.emullen.Connectionless;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.emullen.ProgArgs;

/** The client forms a standard Socket. It sends a message to the server and then waits for a
 *    response, finally exiting once this cycle is complete. */
public class ConnectionlessClient extends ConnectionlessBase {

    public ConnectionlessClient(ProgArgs args) {
        super(args);

        InetAddress serverAddress;
        try {
            serverAddress = InetAddress.getByName(args.getAddress());
        } catch (UnknownHostException e) {
            System.err.println("Failed to resolve address \"" + args.getAddress() + "\"");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        String messageToSend = String.join(" ", args.getUncaughtArgs());

        System.out.println("");
        System.out.println("Running connectionless client.");
        System.out.println("Sending message \"" + messageToSend + "\"");

        sendMessagePacket(messageToSend, serverAddress, args.getPort());

        String response = recieveMessagePacket();

        System.out.println("");
        System.out.println("  " + response);
        System.out.println("");
    }
}