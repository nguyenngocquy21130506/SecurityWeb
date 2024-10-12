package com.commenau.mail;

import java.io.IOException;
import java.util.Properties;

public class MailProperties {
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(MailProperties.class.getClassLoader().getResourceAsStream("Mail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final boolean auth = properties.getProperty("mail.smtp.auth").equals("true");
    public static final boolean starttls = properties.getProperty("mail.smtp.starttls.enable").equals("true");
    public static final String host = properties.getProperty("mail.smtp.host");
    public static final String port = properties.getProperty("mail.smtp.port");
    public static final String user = properties.getProperty("mail.user");
    public static final String password = properties.getProperty("mail.password");

}
