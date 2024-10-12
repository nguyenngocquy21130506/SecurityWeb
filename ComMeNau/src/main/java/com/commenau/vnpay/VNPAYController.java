
package com.commenau.vnpay;

import com.commenau.constant.SystemConstant;
import com.commenau.model.Invoice;
import com.commenau.util.FormUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/vnpay-payment")
public class VNPAYController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Invoice invoice = FormUtil.toModel(Invoice.class, request);
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = (long) ((Long.parseLong(request.getParameter("amount")) +   invoice.getShippingFee()) * 100);
        String bankCode = request.getParameter("NCB");

        String vnp_TxnRef = ConfigVNPAY.getRandomNumber(8);
        String vnp_IpAddr = ConfigVNPAY.getIpAddress(request);

        String vnp_TmnCode = ConfigVNPAY.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

        StringBuilder returnUrlParams = new StringBuilder();
        returnUrlParams.append(ConfigVNPAY.vnp_ReturnUrl).append("?key=").append(URLEncoder.encode(invoice.getEmail(), StandardCharsets.US_ASCII))
                .append("&paymentMethod=").append(URLEncoder.encode(invoice.getPaymentMethod(), StandardCharsets.US_ASCII));
//                .append("&email=").append(URLEncoder.encode(invoice.getEmail(), StandardCharsets.US_ASCII))
//                .append("&address=").append(URLEncoder.encode(invoice.getAddress(), StandardCharsets.UTF_8))
//                .append("&phoneNumber=").append(URLEncoder.encode(invoice.getPhoneNumber(), StandardCharsets.US_ASCII))
//                .append("&note=").append(URLEncoder.encode(invoice.getNote(), StandardCharsets.UTF_8))
//                .append("&fullName=").append(URLEncoder.encode(invoice.getFullName(), StandardCharsets.UTF_8))
//                .append("&shippingFee=").append(URLEncoder.encode(invoice.getShippingFee()+"", StandardCharsets.US_ASCII))
//                .append("&voucherId=").append(URLEncoder.encode(invoice.getVoucherId()+"", StandardCharsets.US_ASCII))
//                .append("&districtCode=").append(URLEncoder.encode(invoice.getDistrictCode()+"", StandardCharsets.US_ASCII))
//                .append("&wardCode=").append(URLEncoder.encode(invoice.getWardCode()+"", StandardCharsets.US_ASCII));
        SystemConstant.waitingPayments.put(invoice.getEmail() , invoice);
        vnp_Params.put("vnp_ReturnUrl", returnUrlParams.toString());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVNPAY.hmacSHA512(ConfigVNPAY.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVNPAY.vnp_PayUrl + "?" + queryUrl;
        com.google.gson.JsonObject job = new JsonObject();
        job.addProperty("code", "00");
        job.addProperty("message", "success");
        job.addProperty("data", paymentUrl);
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(job));
    }

}