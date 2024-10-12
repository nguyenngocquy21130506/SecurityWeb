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
public class InvoiceDTO implements Logable {
    private Integer id;
    private Long userId;
    private Integer voucherId;
    private String userFullName;
    private String userEmail;
    private String checkoutMethod;
    private String status;
    private Double shippingFee;
    private Double total;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phoneNumber;
    private String note;
    private String paymentMethod;
    private Timestamp timeDelivery;
    private Timestamp updatedAt;
    private Timestamp createAt;
    //image,name, list?
    @Override
    public Logable getData() {
        return InvoiceDTO.builder().id(this.id).userId(this.userId).voucherId(this.voucherId).userFullName(this.userFullName).phoneNumber(this.phoneNumber).userEmail(this.userEmail).address(this.address).status(this.status).note(this.note).build();
    }
}