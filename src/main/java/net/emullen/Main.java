package net.emullen;

public class Main {
    public static void main(String[] args) {
        ProgArgs progArgs = new ProgArgs(args);

        if(progArgs.isServer())
            new Server(progArgs);
        else
            new Client(progArgs);
    }
}
