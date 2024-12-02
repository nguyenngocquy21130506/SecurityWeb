package com.commenau.encryptMode;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DESEncryption {
    private static SecretKey secretKey;

    public void setSecretKey(byte[] keyBytes) {
        // Đảm bảo rằng chiều dài của decodedKey là 8 byte (64 bit)
        if (keyBytes.length != 8) {
            throw new IllegalArgumentException("Invalid key length: " + keyBytes.length);
        }
        // Tạo SecretKeySpec với toàn bộ mảng byte
        secretKey = new SecretKeySpec(keyBytes, "DES");
    }

    public static void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        secretKey = keyGenerator.generateKey();
    }

    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] result = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }

    public static String decrypt(String encryptedData) throws Exception {
        // Kiểm tra định dạng OpenSSL
        if (encryptedData.startsWith("U2FsdGVkX1")) {
            // Lấy salt từ chuỗi mã hóa
            byte[] salt = Base64.getDecoder().decode(encryptedData.substring(10, 18)); // 8 bytes salt
            byte[] cipherBytes = Base64.getDecoder().decode(encryptedData.substring(18)); // cipher text

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // In ra kích thước của cipherBytes
            System.out.println("Cipher bytes length: " + cipherBytes.length);

            // Giải mã
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }
        throw new IllegalArgumentException("Invalid encrypted data format");
    }
}
