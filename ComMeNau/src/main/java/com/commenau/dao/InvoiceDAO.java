package com.commenau.dao;

import com.commenau.constant.SystemConstant;
import com.commenau.model.Invoice;
import com.commenau.model.User;
import org.jdbi.v3.core.statement.Update;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import com.commenau.connectionPool.ConnectionPool;

public class InvoiceDAO {

    public List<Invoice> getAllInvoiceById(Long userId) {
        List<Invoice> invoices = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id from invoices where userId = ?")
                    .bind(0, userId)
                    .mapToBean(Invoice.class)
                    .list();
        });
        return invoices;
    }

    public List<Invoice> get10InvoiceById(Long userId) {
        List<Invoice> invoices = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("SELECT id FROM invoices WHERE userId = ? ORDER BY createdAt DESC LIMIT 10 ")
                    .bind(0, userId)
                    .mapToBean(Invoice.class)
                    .list();
        });
        return invoices;
    }

    public Invoice getInvoiceById(int invoiceId) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id,userId,voucherId,fullName,email,province,district,ward,note,shippingFee,address,phoneNumber,paymentMethod from invoices where id = ?")
                    .bind(0, invoiceId)
                    .mapToBean(Invoice.class)
                    .first();
        });
    }

    public List<Invoice> getAllInvoicePaged(int nextPage, int pageSize) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select i.* from invoices i JOIN invoice_status s ON i.id = s.invoiceId ORDER BY s.status DESC ,i.createdAt desc limit ? offset ?")
                    .bind(0, pageSize)
                    .bind(1, (nextPage - 1) * pageSize)
                    .mapToBean(Invoice.class)
                    .list();
        });
    }

    public User getUserBy(int invoiceId) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select * from users where id = (select userId from invoices where id = :id)").bind("id", invoiceId)
                    .mapToBean(User.class).one();
        });
    }

    public List<Invoice> getAllInvoice() {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id from invoices")
                    .mapToBean(Invoice.class)
                    .list();
        });
    }

    public Integer sellingOfDay() {
        String sql = "SELECT COUNT(i.id) FROM invoices i INNER JOIN invoice_status s ON i.id = s.invoiceId " +
                "WHERE DATE(i.createdAt) = CURDATE() AND s.status = '" + SystemConstant.INVOICE_SHIPPED + "'";
        return calculate(sql, Integer.class);
    }

    public Integer sellingOfMonth() {
        String sql = "SELECT COUNT(i.id) FROM invoices i INNER JOIN invoice_status s ON i.id = s.invoiceId " +
                "WHERE MONTH(i.createdAt) = MONTH(CURDATE()) AND s.status = '" + SystemConstant.INVOICE_SHIPPED + "'";
        return calculate(sql, Integer.class);
    }

    public Double revenueOfMonth() {
        String sql = "SELECT SUM(ii.price * ii.quantity) FROM invoices i " +
                "INNER JOIN invoice_items ii ON i.id = ii.invoiceId " +
                "INNER JOIN invoice_status ist ON i.id = ist.invoiceId " +
                "WHERE ist.status = '" + SystemConstant.INVOICE_SHIPPED + "'" +
                "AND YEAR(i.createdAt) = YEAR(CURDATE()) " +
                "AND MONTH(i.createdAt) = MONTH(CURDATE()) ";
        return calculate(sql, Double.class);
    }

    private <T> T calculate(String sql, Class<T> tClass) {
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(tClass)
                        .findFirst()
                        .orElse(null)
        );
    }

    public Double revenueOfDay() {
        String sql = "SELECT SUM(ii.price * ii.quantity) FROM invoices i " +
                "INNER JOIN invoice_items ii ON i.id = ii.invoiceId " +
                "INNER JOIN invoice_status ist ON i.id = ist.invoiceId " +
                "WHERE ist.status = '" + SystemConstant.INVOICE_SHIPPED + "'" +
                "AND YEAR(i.createdAt) = YEAR(CURDATE()) " +
                "AND MONTH(i.createdAt) = MONTH(CURDATE()) " +
                "AND DATE(i.createdAt) = CURDATE()";
        return calculate(sql, Double.class);
    }

    public int save(Invoice invoice) {
        String sql = "INSERT INTO invoices(userId,fullName,shippingFee,note,address,phoneNumber,email, paymentMethod,province,district,ward,wardCode,districtCode, voucherId) VALUES " +
                "(:userId,:fullName,:shippingFee,:note,:address,:phoneNumber,:email, :paymentMethod,:province,:district,:ward,:wardCode,:districtCode,:voucherId)";
        boolean checkNull = invoice.getVoucherId()==null && invoice.getShippingFee()==null;
        int invoceId = ConnectionPool.getConnection().inTransaction(handle -> {
            Update update = handle.createUpdate(sql)
                    .bind("fullName", invoice.getFullName())
                    .bind("shippingFee", invoice.getShippingFee()==null?"0":invoice.getShippingFee())
                    .bind("note", invoice.getNote())
                    .bind("address", invoice.getAddress())
                    .bind("phoneNumber", invoice.getPhoneNumber())
                    .bind("email", invoice.getEmail())
                    .bind("paymentMethod", invoice.getPaymentMethod())
                    .bind("province", invoice.getProvince()==null?"Hồ Chí Minh":invoice.getProvince())
                    .bind("district", invoice.getDistrict()==null?"Thủ Đức":invoice.getDistrict())
                    .bind("ward", invoice.getWard()==null?"Linh Trung":invoice.getWard())
                    .bind("wardCode", invoice.getWardCode())
                    .bind("districtCode", invoice.getDistrictCode())
                    .bind("voucherId", invoice.getVoucherId()==null?null:(invoice.getVoucherId()==0?null:invoice.getVoucherId()));
            if (invoice.getUserId() != null)
                update.bind("userId", invoice.getUserId());
            else update.bindNull("userId", Types.INTEGER);
            return update.executeAndReturnGeneratedKeys("id").mapTo(Integer.class).first();
        });
        return invoceId;
    }

    public void updateTimeDelivery(Timestamp timeDelivery, int invoiceId) {
        JDBIConnector.getInstance().inTransaction(handle -> {
            return handle.createUpdate("UPDATE invoices SET timeDelivery = ? where id = ?")
                    .bind(0,timeDelivery)
                    .bind(1,invoiceId)
                    .execute();
        });
    }
}
