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
public class BlogReview implements Logable {
    private int id;
    private int blogId;
    private long userId;
    private String content;
    private Timestamp createdAt;

    @Override
    public Logable getData() {
        return BlogReview.builder().userId(this.userId).content(this.content).build();
    }
}