package com.project.ecommerce.webhook;

import com.project.ecommerce.dto.PaymentWebhookRequest;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class PaymentWebhookController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/payment")
    public ResponseEntity<?> handlePaymentWebhook(@RequestBody PaymentWebhookRequest request) {
        try {
            // Update payment status
            paymentService.updatePaymentStatus(request.getOrderId(), request.getStatus());

            // Update order status based on payment status
            String orderStatus = "SUCCESS".equals(request.getStatus()) ? "PAID" : "FAILED";
            orderService.updateOrderStatus(request.getOrderId(), orderStatus);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Webhook processed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}