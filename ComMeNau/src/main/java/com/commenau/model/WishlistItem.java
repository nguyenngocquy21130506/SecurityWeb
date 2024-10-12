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
public class WishlistItem implements Logable {
    private int id;
    private long userId;
    private int productId;
    private Timestamp addeddate;

    @Override
    public Logable getData() {
        return WishlistItem.builder().userId(this.userId).productId(this.productId).build();
    }
}