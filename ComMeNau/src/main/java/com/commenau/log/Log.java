package com.commenau.log;

import com.commenau.model.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Log {
    private int id;
    private String endpoint;
    private String status;
    private String method;
    private String address;
    private String country;
    private String region;
    private String city;
    private LogLevel level;
    private String preValue;
    private String value;
    private Timestamp createAt = new Timestamp(System.currentTimeMillis());
    private Timestamp updateAt = new Timestamp(System.currentTimeMillis());
}
