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
public class EmailVerification {
    private int id;
    private long userId;
    private String token;
    private Timestamp createdAt;
    private Timestamp expriedAt;

}
