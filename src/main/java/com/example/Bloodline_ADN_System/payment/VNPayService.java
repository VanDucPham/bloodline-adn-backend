package com.example.Bloodline_ADN_System.payment;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    public String createPaymentUrl(PaymentRequest req, String ip) {
        try {
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(req.getAmount() * 100));
            if (req.getBankCode() != null && !req.getBankCode().isEmpty()) {
                vnp_Params.put("vnp_BankCode", req.getBankCode());
            }
            vnp_Params.put("vnp_CurrCode", "VND");



            vnp_Params.put("vnp_TxnRef", req.getTxnRef());
            vnp_Params.put("vnp_OrderInfo", req.getOrderInfo());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", (req.getLanguage() != null && !req.getLanguage().isEmpty()) ? req.getLanguage() : "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", ip);

            // Format date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // ✅ KHÔNG thêm vnp_SecureHashType vào params để hash
            vnp_Params.remove("vnp_SecureHashType");
            vnp_Params.remove("vnp_SecureHash");

            String hashData = VNPayUtils.hashData(vnp_Params);
            String secureHash = VNPayUtils.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData);
            String queryUrl = VNPayUtils.buildQuery(vnp_Params)

                    + "&vnp_SecureHash=" + secureHash;

            // DEBUG LOG
            System.out.println("========== VNPAY PARAMS ==========");
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("========== RAW HASH DATA ==========");
            System.out.println(hashData);

            System.out.println("========== SECURE HASH ==========");
            System.out.println(secureHash);

            System.out.println("========== PAYMENT URL ==========");
            System.out.println(VNPayConfig.vnp_PayUrl + "?" + queryUrl);

            return VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        } catch (Exception e) {
            throw new RuntimeException("Tạo URL thanh toán thất bại", e);
        }
    }
}
