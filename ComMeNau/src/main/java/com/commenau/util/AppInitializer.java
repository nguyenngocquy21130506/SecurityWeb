package com.commenau.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Sinh ra khóa AES mới (hoặc lấy từ file config nếu có sẵn)
            String secretKey = AESKeyUtil.generateAESKey();

            // Lưu khóa vào ServletContext
            sce.getServletContext().setAttribute("SECRET_KEY", secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup resources if needed
    }
}
