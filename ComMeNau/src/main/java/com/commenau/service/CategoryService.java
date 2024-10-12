package com.commenau.service;

import com.commenau.dao.CategoryDAO;
import com.commenau.dao.ProductDAO;
import com.commenau.dto.CategoryInfoDTO;
import com.commenau.model.Category;
import com.commenau.paging.PageRequest;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    @Inject
    private CategoryDAO categoryDAO;
    @Inject
    private ProductDAO productDAO;

    public List<Category> getAll(PageRequest pageRequest) {
        return categoryDAO.findAll(pageRequest);
    }

    public boolean update(Category category) {
        return categoryDAO.update(category);
    }
    public boolean save(Category category) {
        return categoryDAO.save(category);
    }

    public int getTotalItem() {
        return categoryDAO.totalItem();
    }

    public boolean detele(Integer[] ids) {
        try {
            for (Integer id : ids) {
                //set null for product
                productDAO.updateCategory(id, null);
                //delete category
                categoryDAO.delete(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Return false immediately if any product deletion fails
            return false;
        }
    }
    public List<CategoryInfoDTO> getAllCategoryInfo(){
        List<CategoryInfoDTO> categoryInfoDTOS = new ArrayList<>();

        for(var x: categoryDAO.getAllCategory()){
            CategoryInfoDTO categoryInfoDTO = CategoryInfoDTO.builder().build();
            categoryInfoDTO.setId(x.getId());
            categoryInfoDTO.setName(x.getName());
            categoryInfoDTOS.add(categoryInfoDTO);
        }
        return categoryInfoDTOS;
    }
}
