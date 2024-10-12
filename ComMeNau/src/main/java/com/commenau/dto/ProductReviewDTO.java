package com.commenau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDTO {
    private String firstName;
    private String lastName;
    private Integer rating;
    private String content;
    private Timestamp createdAt;
}
