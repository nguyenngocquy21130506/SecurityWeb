package com.commenau.service;

import com.commenau.dao.ProductReviewDAO;
import com.commenau.dao.UserDAO;
import com.commenau.dto.ProductReviewDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.model.ProductReview;
import com.commenau.model.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ProductReviewService implements Pageable<ProductReviewDTO> {
    @Inject
    ProductReviewDAO dao;
    @Inject
    UserDAO userDAO;


    public List<ProductReview> getProductReviewsByProductId(int id) {
        return dao.getProdudctReviewsByProductId(id);
    }

    public void save(int productId, int userId, int rating, String content) {
        dao.save(productId, userId, rating, content);
    }


    @Override
    public List<ProductReviewDTO> getPage(int id, int size, int page, String sortBy, String sort) {
        List<ProductReview> productReviews = dao.getProdudctPageReviewsByProductId(id, size, page, sortBy, sort);
        List<ProductReviewDTO> productReviewDTOS = new ArrayList<>();
        System.out.println(productReviews);
        for (var x : productReviews) {
            ProductReviewDTO dto = ProductReviewDTO.builder().build();

            dto.setRating(x.getRating());
            dto.setContent(x.getContent());
            dto.setCreatedAt(x.getCreatedAt());

            User user = userDAO.getFirstNameAndLastName(x.getUserId());

            dto.setLastName(user.getLastName());
            dto.setFirstName(user.getFirstName());

            productReviewDTOS.add(dto);

        }


        return productReviewDTOS;
    }

    @Override
    public List<ProductReviewDTO> getPage(int id, int size, int page) {
        List<ProductReview> productReviews = dao.getProdudctPageReviewsByProductId(id, size, page);
        List<ProductReviewDTO> productReviewDTOS = new ArrayList<>();

        for (var x : productReviews) {
            ProductReviewDTO dto = ProductReviewDTO.builder().build();

            dto.setRating(x.getRating());
            dto.setContent(x.getContent());
            dto.setCreatedAt(x.getCreatedAt());

            User user = userDAO.getFirstNameAndLastName((x.getUserId()));

            dto.setLastName(user.getLastName());
            dto.setFirstName(user.getFirstName());

            productReviewDTOS.add(dto);

        }


        return productReviewDTOS;
    }
}
