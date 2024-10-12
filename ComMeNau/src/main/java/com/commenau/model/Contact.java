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
public class Contact implements Logable {
    private int id;
    private String fullName;
    private String email;
    private String message;
    private long userId;
    private Timestamp createdAt;

    @Override
    public Logable getData() {
        return Contact.builder().userId(this.getUserId()).email(this.getEmail()).message(this.getMessage()).build();
    }
}