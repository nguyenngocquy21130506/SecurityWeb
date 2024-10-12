package com.commenau.util;

public class RoundUtil {
    public static long roundPrice(double price) {
        return Math.round(price / 1000) * 1000;
    }
}
