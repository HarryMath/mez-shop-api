package com.mez.api.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public abstract class Encryptor {

    private static final char[] HEX_ARRAY =     "0123456789abcdef".toCharArray();
    private static final char[] STRANGE_ARRAY = "52!%?&7$0a3c#du7".toCharArray();
    public static final String MD5 = "MD5";
    public static final String SHA2 = "SHA-256";

    public static String encrypt(String data, String algorithm) {
        try {
            MessageDigest sha2 = MessageDigest.getInstance(algorithm);
            byte[] hash = sha2.digest(data.getBytes(StandardCharsets.UTF_8));
            return getHex(hash);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return data;
        }
    }

    private static String getHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
