package com.commenau.service;

import com.commenau.model.Invoice;
import com.commenau.model.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderGHNService {


    public Timestamp createOrder(Invoice invoice, Map<Product, Integer> invoiceItems) {
        Gson gson = new Gson();
        try {
            double price = 0;
            List<Map<String, Object>> items = null;
            for (var x : invoiceItems.entrySet()) {
                items = new ArrayList<>();
                Map<String, Object> item = new HashMap<>();
                item.put("name", x.getKey().getName());
                item.put("quantity", x.getValue());
                item.put("price", x.getKey().getPrice() * (1 - x.getKey().getDiscount()));
                item.put("length", 12);
                item.put("width", 12);
                item.put("height", 12);
                item.put("weight", 120);
                price+=x.getKey().getPrice() * (1 - x.getKey().getDiscount());
            }
            // Khởi tạo tham số
            Map<Object, Object> data = new HashMap<>();
            data.put("payment_type_id", 2);
            data.put("note", invoice.getNote());
            data.put("required_note", "KHONGCHOXEMHANG");
            data.put("from_name", "Cơm Mẹ Nấu");
            data.put("from_phone", "0854705932");
            data.put("from_address", "72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam");
            data.put("from_ward_name", "Phường 12");
            data.put("from_district_name", "Quận Bình Thạnh");
            data.put("from_province_name", "HCM");
            data.put("return_phone", "0332190444");
            data.put("return_address", "39 NTT");
            data.put("return_district_id", null);
            data.put("return_ward_code", "");
            data.put("client_order_code", "");
            data.put("to_name", invoice.getFullName());
            data.put("to_phone", invoice.getPhoneNumber());
            data.put("to_address", invoice.getAddress());
            data.put("to_ward_code", invoice.getWardCode() + "");
            data.put("to_district_id", Integer.valueOf(invoice.getDistrictCode()));
            data.put("cod_amount", (int) price);
            data.put("content", "Theo New York Times");
            data.put("weight", 1000);
            data.put("length", 40);
            data.put("width", 60);
            data.put("height", 50);
            data.put("pick_station_id", Integer.valueOf(invoice.getDistrictCode()));
            data.put("deliver_station_id", null);
            data.put("insurance_value", 10000);
            data.put("service_id", 0);
            data.put("service_type_id", 2);
            data.put("coupon", null);

            List<Integer> pickShift = new ArrayList<>();
            pickShift.add(2);
            data.put("pick_shift", pickShift);


            data.put("items", items);
            String json = gson.toJson(data);

            // URL của API
            String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

            // Tạo request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("ShopId", "191505")
                    .header("Token", "b5c8bf1a-ea8c-11ee-8bfa-8a2dda8ec551")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // Gửi request và nhận response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // In ra response

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            String expectedDeliveryTime = jsonObject.getAsJsonObject("data").get("expected_delivery_time").getAsString();
            // Định dạng của chuỗi thời gian
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

            // Chuyển đổi chuỗi thành đối tượng LocalDateTime
            LocalDateTime localDateTime = LocalDateTime.parse(expectedDeliveryTime, formatter);

            // Chuyển đổi đối tượng LocalDateTime thành Timestamp
            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
