package net.emullen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/** The client forms a standard Socket. It sends a message to the server and then waits for a
 *    response, finally exiting once this cycle is complete. */
public class Client {

    public static final long MAX_MESSAGE_LENGTH = 256;

    private InetSocketAddress bindAddress;
    private Socket socket;
    private Message messageToSend;

    public Client(ProgArgs args) {
        this.bindAddress = new InetSocketAddress(args.getAddress(), args.getPort());
        this.messageToSend = new Message(String.join(" ", args.getUncaughtArgs()), !args.isEncrypt());

        // We're taking the length of the serialized message to check if we can send it over the
        //   line since I'm interpreting the prompt as the 256 char limit being a limit on the
        //   sent message size- not the plaintext message size.
        // (Serializing a Message class adds extra characters)
        int msgLen = messageToSend.serialize().length();
        if(msgLen > MAX_MESSAGE_LENGTH) {
            System.err.println("Message length is too long to send to server, remove " + (msgLen - MAX_MESSAGE_LENGTH) + " characters to proceed.");
            System.exit(1);
        }

        if(!bind())
            System.exit(1);

        System.out.println("");
        System.out.println("Sending message \"" + messageToSend.getMessage() + "\"");
        System.out.println(messageToSend.isEncrypted() ? "Server will decrypt message." : "Server will encrypt message.");
        sendMessage();

        Message inMessage = readMessage();
        if(inMessage == null) {
            System.err.println("Failed to read incoming message.");
            System.exit(1);
            return;
        }

        System.out.println("");
        System.out.println("  " + inMessage.getMessage());
        System.out.println("");
    }

    private boolean bind() {
        System.out.println("Binding to address: " + bindAddress);
        try {
            socket = new Socket();
            socket.connect(bindAddress);            
        } catch(IOException e) {
            System.err.println("Failed to bind to client socket on address \"" + bindAddress + "\" (!!!!! is the server running?)");
            return false;
        }

        if(socket == null) {
            System.err.println("Failed to bind to client socket, it was created but is null somehow.");
            return false;
        } else {
            return true;
        }
    }

    private Message readMessage() {
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
    
    private void sendMessage() {
         try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            pw.println(messageToSend.serialize());
            pw.flush();
        } catch (IOException e) {
            System.err.println("Failed to send message.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}