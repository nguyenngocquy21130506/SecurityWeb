package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {
    private int id;
    private long userId;
    private int productId;
    private int rating;
    private String content;
    private Timestamp createdAt;

}