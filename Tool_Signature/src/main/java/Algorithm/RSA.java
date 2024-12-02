//21130506_Nguyễn Ngọc Quý_DH21DTC
package Algorithm;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private SecretKey secretKey;

    public RSA() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        // Tạo khóa AES với chiều dài 256 bit
        KeyGenerator keyGenAES = KeyGenerator.getInstance("AES");
        keyGenAES.init(256); // Hoặc 128 hoặc 192
        secretKey = keyGenAES.generateKey();
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPublicKey(byte[] publicKeyBytes) throws Exception {
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.publicKey = kf.generatePublic(X509publicKey);
    }

    public void setPrivateKey(byte[] privateKeyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(keySpec);
    }

    public String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }

    public boolean encryptFile(String src, String des) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        Cipher cipherAES = Cipher.getInstance("AES");
        cipherAES.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] keyEncrypt = cipher.doFinal(secretKey.getEncoded());

        DataInputStream dis = new DataInputStream(new FileInputStream(src));
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(des));
        CipherOutputStream cos = new CipherOutputStream(dos, cipherAES);

        dos.writeUTF(Base64.getEncoder().encodeToString(keyEncrypt));

        byte[] buf = new byte[1024];
        int i;
        while ((i = dis.read(buf)) != -1) {
            cos.write(buf, 0, i);
        }
        cos.close();
        dos.close();
        dis.close();
        return true;
    }

    public boolean decryptFile(String src, String des) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        DataInputStream dis = new DataInputStream(new FileInputStream(src));
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(des));

        String keyOfFile = dis.readUTF();
        byte[] keyEncrypt = Base64.getDecoder().decode(keyOfFile);

        byte[] aesKey = cipher.doFinal(keyEncrypt);
        SecretKey originalKey = new SecretKeySpec(aesKey, "AES");

        Cipher cipherAES = Cipher.getInstance("AES");
        cipherAES.init(Cipher.DECRYPT_MODE, originalKey);

        CipherInputStream cis = new CipherInputStream(dis, cipherAES);
        byte[] buf = new byte[1024];
        int i;
        while ((i = cis.read(buf)) != -1) {
            dos.write(buf, 0, i);
        }
        dos.flush();
        cis.close();
        dos.close();
        dis.close();

        return true;
    }
}
