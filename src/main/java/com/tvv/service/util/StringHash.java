package com.tvv.service.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringHash {
    public static String getHashString(String input) {
        String outputHash = "";
        if (input==null) throw new IllegalArgumentException();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            BigInteger number = new BigInteger(1, encodedHash);

            // Convert message digest into hex value
            StringBuilder hexString = new StringBuilder(number.toString(16));
            // Pad with leading zeros
            while (hexString.length() < 32)
            {
                hexString.insert(0, '0');
            }
            outputHash =  hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outputHash;
    }

    public static boolean equalsHash (String input, String hashString){
        return hashString.equals(getHashString(input));
    }

}
