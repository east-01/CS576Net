package net.emullen;

import java.util.ArrayList;
import java.util.List;

/** A parser for the provided java arguments String array. This way we can better access the
 *    information provided in the arguments, and we can pass this class around for multiple
 *    use cases. */
public class ProgArgs {

    private List<String> uncaughtArgs;

    private boolean isServer;
    private String address;
    private int port;
    private boolean encrypt;

    public ProgArgs(String[] args) {
        uncaughtArgs = new ArrayList<>();

        // Apply defaults
        address = "localhost";
        port = 444;
        encrypt = true;
        
        // Parse arguments
        String argName = null;
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.startsWith("-")) {
                if(argName != null) {
                    System.err.println("Argument error: No value provided for \"" + argName + "\"");
                    argName = null;
                }

                argName = arg.substring(1);

                if(parseName(argName)) {
                    argName = null;
                    continue;
                }

            } else {
                if(argName == null) {
                    uncaughtArgs.add(arg);
                    continue;
                }

                parsePair(argName, arg);
                argName = null;
            }
        }
    }

    /**
     * Parse an argument name, this is for arguments that won't have values
     * @param argName The argument name to parse
     * @return The argument name was parsed correctly
     */
    private boolean parseName(String argName) {
        switch(argName.toLowerCase()) {
            case "server":
                isServer = true;
                return true;
            case "client":
                isServer = false;
                return true;
            case "encrypt":
                encrypt = true;
                return true;
            case "decrypt":
                encrypt = false;
                return true;
        }

        return false;
    }

    /**
     * Parse an argument name and value pair, this is the default way of handling arguments
     * @param argName The argument name (i.e. port)
     * @param argVal The argument value (i.e. 444)
     */
    private void parsePair(String argName, String argVal) {
        switch(argName.toLowerCase()) {
            case "addr":
            case "address":
                address = argVal;
                break;
            
            case "port":
                try {
                    port = Integer.parseInt(argVal);
                } catch(Exception e) {
                    System.err.println("Failed to parse argument value \"" + argVal + "\" for name \"" + argName + "\" expected an integer port.");
                }
                break;
            
            default:
                System.err.println("Failed to parse argument name \"" + argName + "\"");
                System.exit(1);
                break;
        }
    }

    public List<String> getUncaughtArgs() { return uncaughtArgs; }
    public boolean isServer() { return isServer; }
    public String getAddress() { return address; }
    public int getPort() { return port; }
    public boolean isEncrypt() { return encrypt; }
}