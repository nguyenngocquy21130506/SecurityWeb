package com.commenau.dto;

import com.commenau.model.ReplyContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDTO {
    private int id;
    private String fullName;
    private String email;
    private String message;
    private Timestamp createdAt;
    private ReplyContact reply;
}
