package com.commenau.service;

import com.commenau.dao.CategoryDAO;
import com.commenau.dao.ProductDAO;
import com.commenau.dao.ProductImageDAO;
import com.commenau.dao.WishListDAO;
import com.commenau.dto.WishlistItemDTO;
import com.commenau.model.WishlistItem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class WishlistService {
    @Inject
    private WishListDAO wishlistDAO;
    @Inject
    private ProductDAO productDAO;
    @Inject
    private ProductImageDAO productImageDAO;
    @Inject
    private CategoryDAO categoryDAO;

    public List<WishlistItemDTO> getAllWishlistItemById(Long userId) {
        List<WishlistItemDTO> result = new ArrayList<>();
        for (WishlistItem wi : wishlistDAO.getAllWishlistItemById(userId)) {
            WishlistItemDTO wishlistDTO = WishlistItemDTO.builder()
                    .product(productDAO.findOneById(wi.getProductId()))
                    .categoryName(categoryDAO.getCategoryNameById(wi.getProductId()))
                    .image(productImageDAO.findAvatarByProductId(wi.getProductId()))
                    .build();
            result.add(wishlistDTO);
        }
        return result;
    }

    public boolean existsItem(int userId, int productId) {
        return wishlistDAO.existsItem(productId, userId);
    }

    public void addItem(int userID, int productId) {
        wishlistDAO.saveItem(userID, productId);
    }

    public boolean deleteItem(int userID, int productId) {
        return wishlistDAO.deleteItem(userID, productId);
    }
    public boolean resetAll(Long userId) {
        return wishlistDAO.resetAll(userId);
    }
}
