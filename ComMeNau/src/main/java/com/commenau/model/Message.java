package com.commenau.model;

import com.commenau.log.Logable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Logable {
    private int id;
    private int conversationId;
    private int senderId;
    private int recipientId;
    private boolean viewed;
    private String content;
    private Timestamp sendTime;

    @Override
    public Logable getData() {
        return Message.builder().senderId(this.getSenderId()).content(this.getContent()).build();
    }
}
