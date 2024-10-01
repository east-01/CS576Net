package net.emullen;

public class Encryption {
    /**
     * Apply a shift to the ASCII index of each character, effectively encryptuing a message with
     *   an ASCII caesars cipher
     * @param message The message to encrypt
     * @param shift The amount to shift in the ascii sequence
     * @return A shifted message
     */
    public static String applyShift(String message, int shift) {
        StringBuilder bld = new StringBuilder();
        for(char c : message.toCharArray()) {
            bld.append((char)(c+shift));
        }
        return bld.toString();
    }
}
