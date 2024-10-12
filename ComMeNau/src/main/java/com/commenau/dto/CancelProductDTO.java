package com.commenau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelProductDTO {
    private int id;
    private String productName;
    private String productImage;
    private int quantity;
    private Timestamp canceledAt;
}