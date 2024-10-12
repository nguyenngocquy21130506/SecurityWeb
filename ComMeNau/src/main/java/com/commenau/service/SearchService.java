package com.commenau.service;

import com.commenau.dao.ProductDAO;
import com.commenau.dto.SearchResultDTO;
import com.commenau.model.Product;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SearchService {
    @Inject
    ProductDAO productDAO;
    private List<Product> products;

    public SearchService() {
        productDAO = new ProductDAO();
        products = productDAO.getAllIdAndName();
    }

    public List<SearchResultDTO> search(String query) {
        List<SearchResultDTO> searchResultDTOS = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
                SearchResultDTO searchResultDTO = SearchResultDTO.builder().build();

                searchResultDTO.setProductId(products.get(i).getId());
                searchResultDTO.setProductName(products.get(i).getName());

                searchResultDTOS.add(searchResultDTO);

            }
            if (searchResultDTOS.size() > 6) break;
        }
        return searchResultDTOS;
    }

    public void updateInformationSearching() {
        products = productDAO.getAllIdAndName();
    }

}
