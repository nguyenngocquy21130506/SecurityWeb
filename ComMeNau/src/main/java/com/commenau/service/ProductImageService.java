package com.commenau.service;

import com.commenau.dao.ProductImageDAO;
import com.commenau.model.ProductImage;

import javax.inject.Inject;

public class ProductImageService {
    @Inject
    private ProductImageDAO imageDAO;
    public boolean saveImage(ProductImage productImage) {
        return imageDAO.save(productImage) > 0;
    }
}
