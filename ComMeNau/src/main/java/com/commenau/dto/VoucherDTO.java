package com.commenau.dto;

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
public class VoucherDTO implements Logable {
    private String name;
    private double discount;
    private int status;
    private double applyTo;
    private Timestamp createdAt;
    private Timestamp expriedAt;

    @Override
    public Logable getData() {
        return VoucherDTO.builder().name(this.getName()).discount(this.getDiscount()).status(this.getStatus()).applyTo(this.getApplyTo()).build();
    }
}
