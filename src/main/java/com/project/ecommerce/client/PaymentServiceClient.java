package com.project.ecommerce.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String MOCK_PAYMENT_SERVICE_URL = "http://localhost:8081";

    public String createMockPayment(String orderId, Double amount, String paymentId) {
        try {
            String url = MOCK_PAYMENT_SERVICE_URL + "/payments/create";

            Map<String, Object> request = new HashMap<>();
            request.put("orderId", orderId);
            request.put("amount", amount);
            request.put("paymentId", paymentId);

            String response = restTemplate.postForObject(url, request, String.class);
            return response;
        } catch (Exception e) {
            System.out.println("Mock payment service error: " + e.getMessage());
            return null;
        }
    }
}