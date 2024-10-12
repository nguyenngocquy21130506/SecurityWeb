package com.commenau.dao;

import com.commenau.connectionPool.ConnectionPool;
import com.commenau.dto.VoucherDTO;
import com.commenau.model.Blog;
import com.commenau.model.Voucher;
import com.commenau.model.WishlistItem;

import java.util.List;

public class VoucherDAO {

    public List<Voucher> getAllVoucher() {
        List<Voucher> vouchers = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id, name, discount, status, applyTo, createdAt , expriedAt from vouchers where status = 1").mapToBean(Voucher.class).list();
        });
        return vouchers;

    }

    public List<VoucherDTO> getVoucherDTO() {
        List<VoucherDTO> voucherDTOs = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select name, discount, stauts, applyTo, expriedAt from vouchers").mapToBean(VoucherDTO.class).list();
        });
        return voucherDTOs;
    }

    public VoucherDTO getVoucherById(int voucherId){
        VoucherDTO voucherDTO = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select name,status,applyTo, discount,createdAt, expriedAt from vouchers where id = ?").bind(0,voucherId)
                    .mapToBean(VoucherDTO.class).first();
        });
        return voucherDTO;
    }

    public boolean deleteVoucher(int idVoucher) {
        return ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate("delete from vouchers where id = ?").bind(0,idVoucher)
                    .execute() > 0;
        });
    }
    public boolean save(Voucher voucher) {
        String sql = "INSERT INTO vouchers(name, discount, applyTo, status, expriedAt) VALUES " +
                "(?,?,?,?,?)";
        return ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate(sql)
                    .bind(0,voucher.getName())
                    .bind(1,voucher.getDiscount())
                    .bind(2,voucher.getApplyTo())
                    .bind(3,voucher.getStatus())
                    .bind(4,voucher.getExpriedAt())
                    .execute() > 0;
        });
    }

    public boolean update(Voucher voucher) {
        String sql = "update vouchers set name=?, discount=?, applyTo=?, status=?, expriedAt=? where id =?";
        return ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind(0,voucher.getName())
                        .bind(1,voucher.getDiscount())
                        .bind(2,voucher.getApplyTo())
                        .bind(3,voucher.getStatus())
                        .bind(4,voucher.getExpriedAt())
                        .bind(5,voucher.getId())
                        .execute()) > 0;
    }

    public boolean changeStatusOfVoucher(int voucherId, int newStatus) {
        String sql = "update vouchers set status = :status where id = :id";
        return ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate(sql)
                    .bind("status",newStatus)
                    .bind("id",voucherId)
                    .execute() > 0;
        });
    }
}
