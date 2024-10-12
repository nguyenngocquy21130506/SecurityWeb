package com.commenau.model;

import com.commenau.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChatDTO {
    private int id;
    private String name;
    private ChatMessageDTO message;
}
