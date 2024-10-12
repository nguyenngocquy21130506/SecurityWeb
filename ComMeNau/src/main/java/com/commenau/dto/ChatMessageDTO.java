package com.commenau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String content;
    private int senderId;
    private Timestamp time;
    private boolean viewed;
}
