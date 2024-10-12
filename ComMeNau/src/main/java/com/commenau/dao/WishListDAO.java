package com.commenau.dao;

import com.commenau.model.WishlistItem;

import java.util.List;
import com.commenau.connectionPool.ConnectionPool;
public class WishListDAO {
    public boolean existsItem(int productId, int userId) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select count(*) from wishlists  where wishlists.userId = ? and wishlists.productId = ? ").bind(0, userId).bind(1, productId).mapTo(Integer.class).one() > 0;
        });
    }

    public boolean exists(int userId) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select count(*) from wishlists where wishlists.userId = ?  ").bind(0, userId).mapTo(Integer.class).one() > 0;
        });
    }


    public void saveItem(int userID, int productId) {
        JDBIConnector.getInstance().inTransaction(n -> {
            return n.createUpdate("insert into wishlists (userID,productId) values (? , ?) ").bind(0, userID).bind(1, productId).execute();
        });
    }

    public boolean deleteItem(int userID, int productId) {
        int result = JDBIConnector.getInstance().inTransaction(n -> {
            return n.createUpdate("delete from wishlists where userId = ? and productId = ? ").bind(0, userID).bind(1, productId).execute();
        });
        return result >= 0;
    }


    public boolean deleteByProductId(int productId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM wishlists WHERE productId = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result >= 0;
    }

    public boolean resetAll(Long userId){
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM wishlists WHERE userId = :userId")
                        .bind("userId", userId)
                        .execute()
        );
        return result >= 0;
    }

    public List<WishlistItem> getAllWishlistItemById(Long userId) {
        List<WishlistItem> wishlistItems = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("select productId from wishlists where userId = ?")
                    .bind(0, userId).mapToBean(WishlistItem.class).list();
        });
        return wishlistItems;

    }
}
