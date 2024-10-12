package com.commenau.dao;

import com.commenau.connectionPool.ConnectionPool;
import com.commenau.dto.ChartInfomationDTO;

import java.util.List;

public class ChartDAO {
    public List<ChartInfomationDTO> getSellProduct(String day, String sort) {

        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT \n" +
                    "    p.name,SUM(quantity) as amount\n" +
                    "FROM \n" +
                    "    invoice_status as s\n" +
                    "INNER JOIN \n" +
                    "\tinvoice_items as i\n" +
                    "ON \n" +
                    "\ts.invoiceId = i.invoiceId\n" +
                    "INNER JOIN \n" +
                    "\tproducts as p\n" +
                    "ON \n" +
                    "\tp.id = productId\n" +
                    "WHERE\n" +
                    "\ts.status = 'Đã nhận'\n" +
                    "    AND\n" +
                    "    s.createdAt <= CURDATE()\n" +
                    "    AND\n" +
                    "     s.createdAt > CURDATE() - INTERVAL " + day + "\n" +
                    "GROUP BY productId\n" +
                    "ORDER BY \n" +
                    "     SUM(quantity) " + sort + "\n" +
                    " LIMIT 5;").mapToBean(ChartInfomationDTO.class).stream().toList();
        });
    }

    public List<ChartInfomationDTO> getSellProductBySearch(String search, String day) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT \n" +
                    "    DATE(s.createdAt) as name ,  sum(i.quantity) as amount\n" +
                    "FROM \n" +
                    "    invoice_status as s\n" +
                    "INNER JOIN \n" +
                    "\tinvoice_items as i\n" +
                    "ON \n" +
                    "\ts.invoiceId = i.invoiceId\n" +
                    "INNER JOIN \n" +
                    "\tproducts as p\n" +
                    "ON \n" +
                    "\tp.id = i.productId\n" +
                    "WHERE\n" +
                    "\tp.name = \"" + search + "\"\n" +
                    "\tAND\n" +
                    "    s.createdAt <= CURDATE()\n" +
                    "    AND\n" +
                    "     s.createdAt > CURDATE() - INTERVAL " + day + "\n" +
                    "GROUP BY  DATE(s.createdAt)").mapToBean(ChartInfomationDTO.class).stream().toList();
        });
    }

    public List<ChartInfomationDTO> getCancelProduct(String day) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT \n" +
                    "    p.name,SUM(quantity) as amount\n" +
                    "FROM \n" +
                    "    invoice_status as s\n" +
                    "INNER JOIN \n" +
                    "\tinvoice_items as i\n" +
                    "ON \n" +
                    "\ts.invoiceId = i.invoiceId\n" +
                    "INNER JOIN \n" +
                    "\tproducts as p\n" +
                    "ON \n" +
                    "\tp.id = productId\n" +
                    "WHERE\n" +
                    "\ts.status = 'Đã hủy'\n" +
                    "    AND\n" +
                    "    s.createdAt <= CURDATE()\n" +
                    "    AND\n" +
                    "     s.createdAt > CURDATE() - INTERVAL " + day + "\n" +
                    "GROUP BY productId\n" +
                    "ORDER BY \n" +
                    "     SUM(quantity) DESC\n" +
                    " LIMIT 5;").mapToBean(ChartInfomationDTO.class).stream().toList();
        });
    }

    public List<ChartInfomationDTO> getStockProduct(String day) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT \n" +
                    "    p.name ,  sum(c.quantity) as amount\n" +
                    "FROM cancel_products as c\n" +
                    "JOIN products as p\n" +
                    "ON\n" +
                    "\tc.productId = p.id\n" +
                    "WHERE\n" +
                    "    c.canceledAt <= CURDATE()\n" +
                    "    AND\n" +
                    "     c.canceledAt > CURDATE() - INTERVAL " + day + "\n" +
                    "GROUP BY  p.id\n" +
                    "ORDER BY \n" +
                    "     SUM(c.quantity) DESC\n" +
                    " LIMIT 10;").mapToBean(ChartInfomationDTO.class).stream().toList();
        });
    }

    public List<ChartInfomationDTO> getSuggest(String day) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT name , available as amount from products").mapToBean(ChartInfomationDTO.class).stream().toList();
        });
    }

    public List<ChartInfomationDTO> neverSell(String day) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT \n" +
                    "    p.name ,  1 as amount\n" +
                    "FROM products as p\n" +
                    "WHERE\n" +
                    "\tp.id \n" +
                    "    NOT IN\n" +
                    "\t(Select distinct invoice_items.productId from invoice_status as s join invoice_items  on s.invoiceId = invoice_items.invoiceId  where s.status = 'Đang xử lý'\n" +
                    "    AND\n" +
                    "        s.createdAt <= CURDATE()\n" +
                    "    AND\n" +
                    "     s.createdAt > CURDATE() - INTERVAL " + day + ")").mapToBean(ChartInfomationDTO.class).stream().toList();
        });
    }

    public List<ChartInfomationDTO> againSell(String day) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("SELECT \n" +
                    "    p.name ,  1 as amount\n" +
                    "FROM products as p\n" +
                    "WHERE\n" +
                    "\tp.id \n" +
                    "    NOT IN\n" +
                    "\t(Select distinct invoice_items.productId from invoice_status as s join invoice_items  on s.invoiceId = invoice_items.invoiceId  where s.status = 'Đang xử lý'\n" +
                    "    AND\n" +
                    "        s.createdAt <= CURDATE()\n" +
                    "    AND\n" +
                    "     s.createdAt > CURDATE() - INTERVAL " + day + ")\n" +
                    "     AND\n" +
                    "     p.id \n" +
                    "     IN\n" +
                    "\t(Select distinct invoice_items.productId from invoice_status as s join invoice_items  on s.invoiceId = invoice_items.invoiceId  where s.status = 'Đang xử lý'\n" +
                    "    AND\n" +
                    "     s.createdAt <= CURDATE() - INTERVAL " + day + ")").mapToBean(ChartInfomationDTO.class).stream().toList();
        });

    }
}
