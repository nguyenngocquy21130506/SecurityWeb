package com.commenau.dto;

import com.commenau.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemDTO {
    private Product product;
    private String categoryName;
    private String image;
}
