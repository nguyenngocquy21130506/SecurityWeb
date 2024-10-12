package com.commenau.service;

import com.commenau.dao.EmailVerificationDAO;
import com.commenau.dao.UserDAO;
import com.commenau.model.EmailVerification;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.UUID;

public class EmailVerificationService {
    @Inject
    private EmailVerificationDAO verificationDao;
    @Inject
    private UserDAO userDao;

    public EmailVerification insert(long userId) {
        String token = UUID.randomUUID().toString();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expried = new Timestamp(now.getTime() + 12 * 60 * 60 * 1000);// 12 hours
        EmailVerification verification = EmailVerification.builder().userId(userId).token(token).expriedAt(expried).build();
        if (!verificationDao.insert(verification)) return null;
        return verification;
    }

    public boolean verifyUser(int userId, String token) {
        boolean flag = false;
        EmailVerification verification = verificationDao.findOneByUserIdAndToken(userId, token);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (verification != null && now.before(verification.getExpriedAt())) {
            flag = userDao.activatedUser(userId);
        }
        if (flag)
            verificationDao.deleteToken(verification);
        return flag;
    }

}
