package com.commenau.dao;

import com.commenau.connectionPool.ConnectionPool;
import com.commenau.dto.CartItemDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.model.Cart;
import com.commenau.model.CartItem;

import java.util.List;
import java.util.Optional;

public class CartDAO {

    public List<CartItemDTO> findCartItemByUserId(long userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ci.id AS cartItemId, ci.quantity, p.id AS productId, p.name, cate.name as categoryName,")
                .append("p.price,p.available,p.status , p.discount, pi.image ")
                .append("FROM carts AS c ")
                .append("INNER JOIN cart_items AS ci ON c.id = ci.cartId ")
                .append("INNER JOIN products AS p ON ci.productId = p.id ")
                .append("INNER JOIN categories AS cate ON cate.id = p.categoryId ")
                .append("INNER JOIN product_images AS pi ON pi.productId = p.id ")
                .append("WHERE c.userId = :userId AND isAvatar = :isAvatar");
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql.toString())
                        .bind("userId", userId)
                        .bind("isAvatar", 1)
                        .map((rs, ctx) -> {
                            // mapping form resultSet to object
                            ProductViewDTO product = ProductViewDTO.builder()
                                    .id(rs.getInt("productId"))
                                    .productName(rs.getString("name"))
                                    .price(rs.getDouble("price"))
                                    .discount(rs.getFloat("discount"))
                                    .status(rs.getBoolean("status"))
                                    .images(List.of(rs.getString("image")))
                                    .available(rs.getInt("available"))
                                    .categoryName(rs.getString("categoryName"))
                                    .build();
                            return CartItemDTO.builder().id(rs.getInt("cartItemId"))
                                    .product(product)
                                    .quantity(rs.getInt("quantity"))
                                    .build();
                        }).list()
        );
    }

    public List<CartItemDTO> findCartItemByName(String name) {
        StringBuilder sql = new StringBuilder();
        sql.append(""); // add SQL : find CartItem by Name

        return null;
    }

    public Cart findCartByUserId(long userId) {
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT * FROM carts WHERE userId = :userId")
                        .bind("userId", userId)
                        .mapToBean(Cart.class)
                        .one()
        );
    }

    public Cart findOneById(int id) {
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT * FROM carts WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Cart.class)
                        .one());
    }

    public CartItem findCartItemByProductAndCart(int productId, int cartId) {
        String sql = "SELECT * FROM cart_items WHERE productId = :productId AND cartId=:cartId";
        Optional<CartItem> first = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .bind("cartId", cartId)
                        .mapToBean(CartItem.class)
                        .findFirst());
        return first.orElse(null);
    }

    public Cart save(Cart cart) {
        int cartId = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("INSERT INTO carts(userId) VALUES(:userId)")
                        .bind("userId", cart.getUserId())
                        .executeAndReturnGeneratedKeys().mapTo(Integer.class).one());
        return findOneById(cartId);
    }

    public boolean saveCartItem(CartItem item) {
        String sql = "INSERT INTO cart_items(cartId, productId, quantity) VALUES(:cartId, :productId, :quantity)";
        Integer result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("cartId", item.getCartId())
                        .bind("productId", item.getProductId())
                        .bind("quantity", item.getQuantity())
                        .execute());
        return result > 0;
    }

    public boolean updateCartItem(CartItem itemEntity, long userId) {
        Cart cart = findCartByUserId(userId);
        String sql = "UPDATE cart_items SET quantity =:quantity WHERE productId=:productId AND cartId = :cartId";
        Integer result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("productId", itemEntity.getProductId())
                        .bind("quantity", itemEntity.getQuantity())
                        .bind("cartId", cart.getId())
                        .execute());
        return result > 0;
    }

    public boolean deleteProduct(int productId, long userId) {
        Cart cart = findCartByUserId(userId);
        String sql = "DELETE FROM cart_items WHERE productId = :productId AND cartId = :cartId";
        int result = ConnectionPool.getConnection().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cartId", cart.getId())
                        .bind("productId", productId)
                        .execute());
        return result > 0;
    }

    public boolean deleteProduct(List<Integer> cartsId) {
        StringBuilder s = new StringBuilder();
        if (cartsId.isEmpty()) return false;
        for (var x : cartsId) {
            s.append(x + ",");
        }
        String ids = "(" + s.substring(0, s.length() - 1) + ")";
        String sql = "DELETE FROM cart_items WHERE id in " + ids;
        int result = ConnectionPool.getConnection().withHandle(handle ->
                handle.createUpdate(sql)

                        .execute());
        return result > 0;
    }

    public boolean deleteByProductId(int productId) throws Exception {
        try {
            int result = ConnectionPool.getConnection().inTransaction(handle ->
                    handle.createUpdate("DELETE FROM cart_items WHERE productId = :productId")
                            .bind("productId", productId)
                            .execute()
            );
            return result >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAll(long userId) {
        String sql = "DELETE ci FROM cart_items ci INNER JOIN carts c ON c.id = ci.cartId WHERE c.userId = :userId";
        int result = ConnectionPool.getConnection().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .execute());
        return result > 0;

    }

    public Cart getCartFromIdItem(int itemId) {
        Optional<Cart> cart = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select * from carts where id in (select cartId from cart_items where id = ?)").bind(0, itemId)
                    .mapToBean(Cart.class).findFirst();
        });
        return cart.orElse(null);
    }

    public boolean changeInvoice(long userId, int voucherId) {
        return ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate("update carts set voucherId = :voucherId where userId = :userId")
                    .bind("voucherId",voucherId)
                    .bind("userId",userId)
                    .execute() > 0;
        });
    }

    public boolean removeInvoice(long userId) {
        return ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate("update carts set voucherId = null where userId = :userId")
                    .bind("userId",userId)
                    .execute() > 0;
        });
    }

    public int getNumCartItem(long userId) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select ct.id from cart_items ct join carts c on ct.cartId = c.id where c.userId = ?")
                    .bind(0,userId)
                    .mapToBean(CartItem.class).list().size();
        });
    }
}
