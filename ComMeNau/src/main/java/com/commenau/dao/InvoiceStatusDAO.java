package com.commenau.dao;

import com.commenau.model.Invoice;
import com.commenau.model.InvoiceStatus;
import com.commenau.connectionPool.ConnectionPool;

public class InvoiceStatusDAO {

    public InvoiceStatus getStatusByInvoice(int invoiceId) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select status, createdAt from invoice_status where invoiceId = ?")
                    .bind(0, invoiceId)
                    .mapToBean(InvoiceStatus.class)
                    .stream().findFirst().orElse(null);
        });
    }

    public boolean setStatus(int invoiceId, String status) {
        String sql = "INSERT INTO invoice_status (invoiceId, status) VALUES(:invoiceId, :status)";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("invoiceId", invoiceId).execute());
        return result > 0;
    }

    public boolean changeStatus(int invoiceId, String status) {
//        String check = ConnectionPool.getConnection().withHandle(n -> {
//           return n.createQuery("select status from invoice_status where invoiceId = :invoiceId").bind("invoiceId", invoiceId).mapTo(String.class).findOne().get();
//        });
//        if(check.equalsIgnoreCase("Đã giao")) return false;
        String sql = "UPDATE invoice_status SET status = :status WHERE invoiceId = :invoiceId";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("invoiceId", invoiceId).execute());
        return result > 0;
    }
}
