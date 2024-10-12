package com.commenau.model;

import com.commenau.log.Logable;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invoice implements Logable {
	private Integer id;
	private Long userId;
	private Integer voucherId;
	private Double shippingFee;
	private String note;
	private String fullName;
	private String province;
	private String district;
	private int districtCode;
	private String ward;
	private int wardCode;
	private String address;
	private String phoneNumber;
	private String email;
	private String paymentMethod;
	private Timestamp timeDelivery;
	private Timestamp createAt;

	@Override
	public Logable getData() {
		return this;
	}
}