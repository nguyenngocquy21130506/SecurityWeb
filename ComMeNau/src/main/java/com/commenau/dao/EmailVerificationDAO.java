package com.commenau.dao;

import com.commenau.connectionPool.ConnectionPool;
import com.commenau.model.EmailVerification;

import java.util.Optional;

public class EmailVerificationDAO {
    public boolean insert(EmailVerification verification) {
        try {
            String sql = "INSERT INTO email_verifications(userId, token, expriedAt) VALUES (:userId, :token, :expriedAt)";
            boolean success = ConnectionPool.getConnection().inTransaction(handle -> {
                handle.createUpdate(sql)
                        .bindBean(verification)
                        .execute();
                return true;
            });

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public EmailVerification findOneByUserIdAndToken(long userId, String token) {
        String sql = "SELECT * FROM email_verifications WHERE userId = :userId AND token = :token";
        Optional<EmailVerification> verification = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("token", token)
                        .mapToBean(EmailVerification.class)
                        .stream()
                        .findFirst());
        return verification.orElse(null);
    }

    public boolean deleteToken(EmailVerification verification) {
        String sql = "DELETE FROM email_verifications WHERE userId = :userId AND token = :token";
        int result = ConnectionPool.getConnection().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(verification)
                        .execute());
        return result > 0;
    }
}
