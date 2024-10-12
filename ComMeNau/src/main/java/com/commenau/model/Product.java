package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private int id;
    private int categoryId;
    private String name;
    private String description;
    private double price;
    private float discount;
    private int view;
    private int available;
    private boolean status;
    private float rate;
    private Timestamp updatedAt;
    private Timestamp createAt;


}