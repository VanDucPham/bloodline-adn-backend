package com.example.Bloodline_ADN_System.payment;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPayUtils {

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hash = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi khi mã hóa HMAC SHA512", ex);
        }
    }

    public static String hashData(Map<String, String> fields) {
        SortedMap<String, String> sortedFields = new TreeMap<>(fields); // tự động sort
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedFields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                sb.append('&');
            }
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1); // xóa dấu '&' cuối cùng
        return sb.toString();
    }







    public static String buildQuery(Map<String, String> params) {
        Map<String, String> sorted = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            sb.append('=');
            sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            sb.append('&');
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
