package com.commenau.dto;

import com.commenau.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogReviewDTO {
    private User user;
    private String content;
    private Timestamp createdAt;
}
