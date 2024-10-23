package com.commenau.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class AESKeyUtil {
    // Method to generate AES SecretKey
    public static String generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);  // Key size
        SecretKey secretKey = keyGen.generateKey();

        // Convert SecretKey to Base64 string
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Method to get SecretKey from Base64 string
    public static SecretKey getAESKeyFromString(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        return new javax.crypto.spec.SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
