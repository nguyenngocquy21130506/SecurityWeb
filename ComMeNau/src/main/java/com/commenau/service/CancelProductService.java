package com.commenau.service;

import com.commenau.dao.CancelProductDAO;
import com.commenau.dto.CancelProductDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.model.Blog;
import com.commenau.model.CancelProduct;
import com.commenau.paging.PageRequest;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CancelProductService {
    @Inject
    private CancelProductDAO cancelDAO;
    @Inject
    private ProductService productService;

    public int countAll() {
        return cancelDAO.countAll();
    }

    public int countByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return countAll();
        return cancelDAO.countByKeyWord(keyWord);
    }
    public List<CancelProductDTO> getByKeyWord(PageRequest pageRequest, String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return getAll(pageRequest);
        else {
            List<CancelProductDTO> results = new ArrayList<>();
            List<CancelProduct> list = cancelDAO.findByKeyWord(pageRequest, keyWord.trim());
            list.forEach(item -> {
                ProductViewDTO viewDTO = productService.getByIdWithAvatar(item.getProductId());
                CancelProductDTO dto = CancelProductDTO.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .canceledAt(item.getCanceledAt())
                        .productName(viewDTO.getProductName())
                        .productImage(viewDTO.getImages().get(0))
                        .build();
                results.add(dto);
            });
            return results;
        }
    }
    public List<CancelProductDTO> getAll(PageRequest pageRequest) {
        List<CancelProductDTO> results = new ArrayList<>();
        List<CancelProduct> list = cancelDAO.findAll(pageRequest);
        list.forEach(item -> {
            ProductViewDTO viewDTO = productService.getByIdWithAvatar(item.getProductId());
            CancelProductDTO dto = CancelProductDTO.builder()
                    .id(item.getId())
                    .quantity(item.getQuantity())
                    .canceledAt(item.getCanceledAt())
                    .productName(viewDTO.getProductName())
                    .productImage(viewDTO.getImages().get(0))
                    .build();
            results.add(dto);
        });

        return results;
    }

    public boolean delete(Integer[] ids) {
        return cancelDAO.delete(ids);
    }


}
