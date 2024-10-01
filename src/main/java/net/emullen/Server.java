package net.emullen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/** The server side makes a ServerSocket and accepts a single socket connection at a time.
 *  Once the server connects to a client socket, the server will expect a message to either encrypt
 *    or decrypt. This is determined using the Message class as the raw string passed through the
 *    socket needs to be deserialized first.
 *  The server will send its response and close the connection.
 */
public class Server {

    private ProgArgs args;
    private InetSocketAddress bindAddress;
    private ServerSocket socket;

    public Server(ProgArgs args) {
        this.args = args;
        this.bindAddress = new InetSocketAddress(args.getPort());

        if(!bind()) {
            System.exit(1);
        }

        System.out.println("Starting connection cycle, press Ctrl+C to exit.");
        // This connection cycle will run indefinitely so the server can handle any series of messages.
        while(true) {
            System.out.println("");

            // Start by connecting and recieving a message
            Socket clientSocket = connect();
            Message inMessage = readMessage(clientSocket);

            if(inMessage == null) {
                System.err.println("Failed to read incoming message. Closing socket.");
                disconnect(clientSocket);
                continue;
            }    

            // Formulate response and send it
            Message outMessage = new Message(
                Encryption.applyShift(inMessage.getMessage(), inMessage.isEncrypted() ? -1 : 1),
                !inMessage.isEncrypted()
            );
            sendMessage(clientSocket, outMessage);

            // Print status and disconnect
            System.out.println("Completed message transaction for client \"" + clientSocket.getInetAddress() + ".\" Details:");
            System.out.println("  In: \"" + inMessage.getMessage() + "\"");
            System.out.println("  Out: \"" + outMessage.getMessage() + "\"");

            disconnect(clientSocket);
        }
    }

    private boolean bind() {
        System.out.println("Binding to address: " + bindAddress);
        try {
            socket = new ServerSocket();
            socket.bind(bindAddress);
        } catch(IOException e) {
            System.err.println("Failed to bind to server socket on address \"" + args.getAddress() + "\"");
            e.printStackTrace();
            return false;
        }

        if(socket == null) {
            System.err.println("Failed to bind to server socket, it was created but is null somehow.");
            return false;
        } else {
            return true;
        }
    }  

    private Socket connect() {
        System.out.println("Listening for connection...");
        try {
            return socket.accept();
        } catch(Exception e) {
            System.err.println("Failed to make connection.");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private void disconnect(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close client socket, exiting.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Message readMessage(Socket socket) {
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Message inMsg = new Message(clientReader.readLine());
            if(inMsg.getMessage() == null) {
                return null;
            }

            return inMsg;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessage(Socket socket, Message msg) {
        try {
            PrintWriter outMsg = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            outMsg.println(msg.serialize());
            outMsg.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
