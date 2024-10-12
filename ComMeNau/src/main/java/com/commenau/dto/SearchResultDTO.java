package com.commenau.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchResultDTO {
    private String productName;
    private int productId;
}
