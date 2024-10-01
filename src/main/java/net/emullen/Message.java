package net.emullen;

/** A wrapper for the raw strings that we are passing through the lines, this allows us to send
 *    more information than just a single string.
 *  Deserialize a string coming in from the network by adding it to the Message(String)
 *    constructor, then you will be able to access it's information. Convert the Message object
 *    back into a raw string with Message#serialize(). */
public class Message {

    public static String MSG_SEPARATOR = "#SEP#";

    private String message;
    private boolean encrypted;

    /** Instantiate a Message with a serialized string, it will attempt to fill out all members
     *    with the passed in string. */
    public Message(String rawString) {
        if(rawString == null) {
            System.err.println("Can't deserialize raw message, it is null.");
            return;
        }
        String[] args = rawString.split(MSG_SEPARATOR);
        if(args.length != 2) {
            System.err.println("Can't deserialize raw message, it splits into a number other than two arguments.");
            System.err.println("Recieved: \"" + rawString + "\"");
            return;
        }

        try {
            encrypted = Boolean.parseBoolean(args[1]);
        } catch(Exception e) {
            System.err.println("Can't deserialize raw message, encrypted argument can't parse as boolean, recieved: \"" + args[1] + "\"");
            return;
        }
        message = args[0];
    }

    public Message(String message, boolean encrypted) {
        this.message = message;
        this.encrypted = encrypted;
    }

    public String serialize() {
        return message + MSG_SEPARATOR + encrypted;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
}
