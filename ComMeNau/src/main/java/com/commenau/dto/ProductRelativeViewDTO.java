package com.commenau.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ProductRelativeViewDTO {
    private int id;
    private String categoryName;
    private String productName;
    private double price;
    private double discount;
    private int rating;
    private int amountOfReview;
    private String image;
}
