package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class Rule {
    private String sql;
    private Map<String,String> data;
    private String alter;
}
