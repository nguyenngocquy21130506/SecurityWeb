package com.commenau.dto;

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
public class LogDTO {
    private String ipAddress;
    private String endpoint;
    private int level;
    private String status;
    private String preValue;
    private String value;
    private Timestamp createAt;
}
