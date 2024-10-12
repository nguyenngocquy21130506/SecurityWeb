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
public class Blog implements Logable {
    private int id;
    private String title;
    private String shortDescription;
    private String content;
    private String image;
    private String createdBy;
    private int numReviews;
    private Timestamp updatedAt;
    private Timestamp createdAt;

    @Override
    public Logable getData() {
        return Blog.builder().title(this.getTitle()).shortDescription(this.getShortDescription()).build();
    }
}