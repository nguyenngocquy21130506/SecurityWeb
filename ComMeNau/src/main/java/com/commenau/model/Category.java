package com.commenau.model;

import com.commenau.log.Logable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Logable {
    private int id;
    private String code;
    private String name;

    @Override
    public Logable getData() {
        return Category.builder().code(this.getCode()).name(this.getName()).build();
    }
}