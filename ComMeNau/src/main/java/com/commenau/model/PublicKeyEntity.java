package com.commenau.model;

import com.commenau.log.Logable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicKeyEntity implements Logable {
    long id;
    String publicKey;
    LocalDateTime expired;
    byte status;
    LocalDateTime createAt;
    long userId;
    String name;

    @Override
    public Logable getData() {
        return PublicKeyEntity.builder()
                .id(this.id)
                .publicKey(this.publicKey)
                .expired(this.expired)
                .status(this.status)
                .createAt(this.createAt)
                .userId(this.userId)
                .name(this.name)
                .build();
    }

}