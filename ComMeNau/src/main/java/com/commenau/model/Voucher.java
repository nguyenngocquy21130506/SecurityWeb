package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    private int id;
    private String name;
    private double discount;
    private double applyTo;
    private int status;
    private Timestamp createdAt;
    private Timestamp expriedAt;

    public int remainTime() {
        // Lấy thời điểm hiện tại
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // Nếu expiredAt trước ngày hiện tại
        if (expriedAt.before(currentTime)) {
            return 0;
        } else {
            // Chuyển đổi expiredAt và currentTime thành kiểu dữ liệu Date
            Date expiredDate = new Date(expriedAt.getTime());
            Date currentDate = new Date(currentTime.getTime());

            // Tính số ngày còn lại
            long diffInMillies = Math.abs(expiredDate.getTime() - currentDate.getTime());
            long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
            if (diffInDays < 1) {
                long diffInHours = diffInMillies / (1000 * 60 * 60);
                return (int) diffInHours;
            } else {
                return (int) diffInDays;
            }
        }
    }

    public boolean checkDay() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // Chuyển đổi expiredAt và currentTime thành kiểu dữ liệu Date
        Date expiredDate = new Date(expriedAt.getTime());
        Date currentDate = new Date(currentTime.getTime());
        long diffInMillies = expiredDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        if (expiredDate.compareTo(currentDate) > 0) {
            if (diffInDays < 1) {
                return false;
            }else{
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean inHour() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // Chuyển đổi expiredAt và currentTime thành kiểu dữ liệu Date
        Date expiredDate = new Date(expriedAt.getTime());
        Date currentDate = new Date(currentTime.getTime());

        // Tính số ngày còn lại
        long diffInMillies = expiredDate.getTime() - currentDate.getTime();
        long diffInHour = diffInMillies / (1000 * 60 * 60);
        if (diffInHour < 1) {
            return true;
        } else {
            return false;
        }
    }
}
