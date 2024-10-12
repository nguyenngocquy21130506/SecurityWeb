package com.commenau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewDTO {
    private int id;
    private int categoryId;
    private String avatar;
    private String categoryName;
    private String productName;
    private String description;
    private int available;
    private int view;
    private double price;
    private float discount;
    private int rating;
    private int amountOfReview;
    private boolean status;
    private boolean wishlist;
    private List<String> images;

}
