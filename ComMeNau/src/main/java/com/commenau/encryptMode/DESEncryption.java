package com.commenau.encryptMode;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DESEncryption {
    private SecretKey secretKey;

    public void setSecretKey(byte[] secretKey) {
        // Đảm bảo rằng chiều dài của decodedKey là 8 byte (64 bit)
        if (secretKey.length != 8) {
            throw new IllegalArgumentException("Invalid key length: " + secretKey.length);
        }
        // Tạo SecretKeySpec với toàn bộ mảng byte
        this.secretKey = new SecretKeySpec(secretKey, "DES");
    }

    public DESEncryption() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56); // DES key length is 56 bits
        secretKey = keyGen.generateKey();
    }

    public String getSecretKey() {
        String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key: " + key);
        return key;
    }

    public byte[] encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] result = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encode(result);
    }

    //    public String decrypt(byte[] data) throws Exception {
//        Cipher cipher = Cipher.getInstance("DES");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decode = Base64.getDecoder().decode(data);
//        byte[] decrypt = cipher.doFinal(decode);
//        return new String(decrypt);
//    }
    public String decrypt(String encryptedData) throws Exception {
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
