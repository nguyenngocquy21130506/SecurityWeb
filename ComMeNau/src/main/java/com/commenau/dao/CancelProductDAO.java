package com.commenau.dao;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CancelProductDTO;
import com.commenau.model.CancelProduct;
import com.commenau.paging.PageRequest;
import com.commenau.util.PagingUtil;
import org.apache.commons.lang3.StringUtils;

import javax.management.Query;
import java.util.Arrays;
import java.util.List;
import com.commenau.connectionPool.ConnectionPool;
public class CancelProductDAO {

    public void cancelProduct() {
        try {
            String sql = "INSERT INTO cancel_products (productId, quantity) " +
                    "SELECT id, available FROM products WHERE available > 0 AND status =:status";
            ConnectionPool.getConnection().inTransaction(handle ->
                    handle.createUpdate(sql).bind("status", SystemConstant.IN_BUSINESS).execute());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countAll() {
        return ConnectionPool.getConnection().withHandle(handle ->
                        handle.createQuery("SELECT COUNT(id) FROM cancel_products"))
                .mapTo(Integer.class).stream().findFirst().orElse(0);
    }

    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(DISTINCT p.id) FROM cancel_products c INNER JOIN products p ON p.id =c.productId " +
                "WHERE quantity like :keyWord OR p.name LIKE :keyWord OR CONVERT(canceledAt, CHAR) LIKE :keyWord";
        return ConnectionPool.getConnection().withHandle(handle ->
                        handle.createQuery(sql))
                .bind("keyWord", "%" + keyWord + "%")
                .mapTo(Integer.class).stream().findFirst().orElse(0);
    }

    public List<CancelProduct> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT c.id, productId, quantity, canceledAt ");
        builder.append("FROM cancel_products c INNER JOIN products p ON p.id = c.productId ");
        builder.append("WHERE quantity LIKE :keyWord OR p.name LIKE :keyWord OR CONVERT(canceledAt, CHAR) LIKE :keyWord ");
        builder.append("GROUP BY productId ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyWord", "%" + keyWord + "%")
                        .mapToBean(CancelProduct.class).stream().toList()
        );
    }

    public List<CancelProduct> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM cancel_products ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(CancelProduct.class).stream().toList()
        );
    }

    public boolean delete(Integer[] ids) {
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("DELETE FROM cancel_products WHERE id IN (<listId>)")
                        .bindList("listId", Arrays.asList(ids)).execute()
        );
        return result > 0;
    }

    public boolean deleteByProductId(Integer productId) throws Exception {
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("DELETE FROM cancel_products WHERE productId = :productId")
                        .bind("productId", productId).execute()
        );
        return result > 0;
    }


}
