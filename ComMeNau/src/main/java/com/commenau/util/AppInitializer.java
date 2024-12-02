package com.commenau.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Sinh ra cặp khóa RSA mới
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048); // Độ dài khóa RSA
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // Lấy Public Key và Private Key
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Lưu Public Key và Private Key vào ServletContext
            sce.getServletContext().setAttribute("PUBLIC_KEY", publicKey);
            sce.getServletContext().setAttribute("PRIVATE_KEY", privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup resources if needed
    }
}