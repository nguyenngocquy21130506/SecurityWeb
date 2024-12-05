package com.commenau.encryptMode;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class DESEncryption {
    private SecretKey secretKey;

    private static final String IV = "ivStatic";

    public String getSecretKey() {
        byte[] keyBytes = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        secretKey = keyGenerator.generateKey();
    }

    public String encrypt(String message) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,iv);
        byte[] result = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }

    public String decrypt(String encryptedData) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] byteDecoded = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(byteDecoded);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    // js [84, 87, 110, 105, 99, 88, 55, 85, 79, 80, 81, 61]
    // jv [84, 87, 110, 105, 99, 88, 55, 85, 79, 80, 81, 61]
    public static void main(String[] args) {
        try {
            DESEncryption desEncryption = new DESEncryption();
            String message = "a";
            desEncryption.setSecretKey(new SecretKeySpec("FsG5ZHbV".getBytes(StandardCharsets.UTF_8), "DES"));

//            String encryptedMessage = desEncryption.encrypt(message);
            String decryptedMessage = desEncryption.decrypt("sctI1McEyNI=");
//            System.out.println("Mã hóa thành công: " + encryptedMessage);
            System.out.println("Giải mã thành công: " + decryptedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TWnicX7UOPQ=
        // TWnicX7UOPQ=
    }
}
