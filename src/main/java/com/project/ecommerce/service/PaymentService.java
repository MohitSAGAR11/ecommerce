package com.project.ecommerce.service;

import com.project.ecommerce.dto.PaymentRequest;
import com.project.ecommerce.model.Order;
import com.project.ecommerce.model.Payment;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String MOCK_PAYMENT_SERVICE_URL = "http://localhost:8081/payments/create";

    public Payment createPayment(PaymentRequest request) {
        // Validate order exists
        Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());

        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order order = orderOpt.get();

        // Check if order is in CREATED status
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order is not in valid state for payment");
        }

        // Generate payment ID
        String paymentId = "pay_mock_" + UUID.randomUUID().toString();

        // Create payment record
        Payment payment = new Payment(
                request.getOrderId(),
                request.getAmount(),
                "PENDING",
                paymentId
        );
        payment = paymentRepository.save(payment);

        // Call mock payment service
        try {
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("orderId", request.getOrderId());
            paymentData.put("amount", request.getAmount());
            paymentData.put("paymentId", paymentId);

            restTemplate.postForObject(MOCK_PAYMENT_SERVICE_URL, paymentData, String.class);
        } catch (Exception e) {
            // Mock service might not be running, continue anyway
            System.out.println("Mock payment service not available: " + e.getMessage());
        }

        return payment;
    }

    public Payment updatePaymentStatus(String orderId, String status) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);

        if (paymentOpt.isEmpty()) {
            throw new RuntimeException("Payment not found for order: " + orderId);
        }

        Payment payment = paymentOpt.get();
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}