package com.commenau.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryInfoDTO {
    private String name;
    private int id;
}
