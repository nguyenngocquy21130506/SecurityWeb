package com.commenau.dao;

import com.commenau.connectionPool.ConnectionPool;
import com.commenau.model.PublicKeyEntity;

import java.util.List;
import java.util.Optional;

public class PublicKeyDao {

    // Phương thức insert PublicKey
    public boolean save(PublicKeyEntity publicKeyEntity) {
        String sql = "INSERT INTO public_key (public_key, expired, status, user_id, name) " +
                "VALUES (:publicKey, :expired, :status, :userId, :name)";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("publicKey", publicKeyEntity.getPublicKey())
                        .bind("expired", publicKeyEntity.getExpired())
                        .bind("status", publicKeyEntity.getStatus())
                        .bind("userId", publicKeyEntity.getUserId())
                        .bind("name", publicKeyEntity.getName())
                        .execute()
        );
        return result > 0;
    }

    // Lấy PublicKey theo ID
    public Optional<PublicKeyEntity> findById(long id) {
        String sql = "SELECT id, public_key, expired, status, created, user_id, name " +
                "FROM public_key WHERE id = :id";
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapToBean(PublicKeyEntity.class)
                        .findOne()
        );
    }

    // Lấy tất cả PublicKey
    public List<PublicKeyEntity> findAll() {
        String sql = "SELECT id, public_key, expired, status, created, user_id, name FROM public_key";
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(PublicKeyEntity.class)
                        .list()
        );
    }

    // Xóa PublicKey theo ID
    public boolean deleteById(long id) {
        String sql = "DELETE FROM public_key WHERE id = :id";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
        return result > 0;
    }

    // Cập nhật PublicKey
    public boolean update(PublicKeyEntity publicKeyEntity) {
        String sql = "UPDATE public_key SET public_key = :publicKey, expired = :expired, " +
                "status = :status, user_id = :userId, name = :name WHERE id = :id";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("publicKey", publicKeyEntity.getPublicKey())
                        .bind("expired", publicKeyEntity.getExpired())
                        .bind("status", publicKeyEntity.getStatus())
                        .bind("userId", publicKeyEntity.getUserId())
                        .bind("name", publicKeyEntity.getName())
                        .bind("id", publicKeyEntity.getId())
                        .execute()
        );
        return result > 0;
    }
}