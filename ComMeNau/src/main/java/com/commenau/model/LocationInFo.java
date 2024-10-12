package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationInFo {
    private String ip;
    private String countryCode;
    private String countryName;
    private String regionName;
    private String cityName;
    private double latitude;
    private double longitude;
    private String zipCode;
    private String timeZone;
    private String asn;
    private String as;
    private boolean isProxy;
}
