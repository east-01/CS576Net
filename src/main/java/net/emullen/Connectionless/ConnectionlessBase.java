package net.emullen.Connectionless;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.emullen.ProgArgs;

public class ConnectionlessBase {

    protected ProgArgs args;
    protected byte[] buffer;
    protected DatagramSocket socket;

    public ConnectionlessBase(ProgArgs args) 
    {
        this.args = args;
        this.buffer = new byte[1024];

        try {
            if(args.isServer())
                socket = new DatagramSocket(args.getPort());
            else
                socket = new DatagramSocket();
        } catch(IOException e) {
            System.err.println("Failed to create DatagramSocket.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected void sendPacket(DatagramPacket outgoingPacket) {
        try {
            socket.send(outgoingPacket);
        } catch(IOException e) {
            System.err.println("Failed to send outgoing packet.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected void sendMessagePacket(String message, InetAddress address, int port) {
        byte[] sendData = message.getBytes();
        sendPacket(new DatagramPacket(sendData, sendData.length, address, port));
    }

    protected DatagramPacket recievePacket() {
        System.out.println("Listening for packet...");
        try {
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(incomingPacket);
            return incomingPacket;
        } catch(Exception e) {
            System.err.println("Failed to make connection.");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    protected String recieveMessagePacket() {
        DatagramPacket incomingPacket = recievePacket();
        return new String(incomingPacket.getData(), 0, incomingPacket.getLength());
    }

}