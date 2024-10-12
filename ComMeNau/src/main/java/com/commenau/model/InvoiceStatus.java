package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceStatus {
    private Integer id;
    private Integer invoiceId;
    private String status;
    private Timestamp createdAt;
}
