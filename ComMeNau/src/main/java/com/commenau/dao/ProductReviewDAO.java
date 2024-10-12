package com.commenau.dao;

import com.commenau.model.ProductReview;

import java.util.List;
import com.commenau.connectionPool.ConnectionPool;
public class ProductReviewDAO {
    private static String getAllPropertiesProductReviewsById = "select * from product_reviews where productId = ? ";
    private static String getAllPropertiesPageProductReviewsById = "select p.rating , p.content , p.createdAt , p.userId  from product_reviews as p  where  p.productId =  ? \n" +
            "limit ? offset ?";


    public List<ProductReview> getProdudctReviewsByProductId(int id) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery(getAllPropertiesProductReviewsById).bind(0, id).mapToBean(ProductReview.class).stream().toList();
        });
    }

    public void save(int productId, int userId, int rating, String content) {
        ConnectionPool.getConnection().withHandle(n -> {
            return n.createUpdate("insert into product_reviews(productId,userId , rating , content) values (?,?,?,?)").bind(0, productId).bind(1, userId).bind(2, rating).bind(3, content).execute();
        });
    }

    public List<ProductReview> getProdudctPageReviewsByProductId(int id, int size, int page) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery(getAllPropertiesPageProductReviewsById).bind(0, id).bind(1, size).bind(2, (page - 1) * size).mapToBean(ProductReview.class).stream().toList();
        });
    }

    public List<ProductReview> getProdudctPageReviewsByProductId(int id, int size, int page, String sortBy, String sort) {
        return ConnectionPool.getConnection().withHandle(n -> {
            String getAllPropertiesPageSortableProductReviewsById = "select p.rating , p.content , p.createdAt , p.userId  from product_reviews as p where p.productId =  ? order by " + sortBy + " " + sort + "  limit ? offset ?";
            return n.createQuery(getAllPropertiesPageSortableProductReviewsById).bind(0, id).bind(1, size).bind(2, (page - 1) * size).mapToBean(ProductReview.class).stream().toList();
        });
    }

    public boolean deleteByProductId(int productId) throws Exception{
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("DELETE FROM product_reviews WHERE productId = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result >= 0;
    }

    public static void main(String[] args) {
        new ProductReviewDAO().getProdudctPageReviewsByProductId(38, 3, 1, "rating", "asc");
    }


}
