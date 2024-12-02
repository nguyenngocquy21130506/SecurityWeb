package com.commenau.util;

import java.io.BufferedReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtil {
    private String value;

    public HttpUtil(String value) {
        this.value = value;
        System.out.println("Request body: " +value);
    }

    public <T> T toModel(Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(this.value, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toModel(String value, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(value, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpUtil of(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new HttpUtil(sb.toString());
    }

}
