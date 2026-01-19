package com.project.ecommerce.dto;

public class PaymentWebhookRequest {
    private String orderId;
    private String status;
    private String paymentId;

    // Constructors
    public PaymentWebhookRequest() {}

    public PaymentWebhookRequest(String orderId, String status, String paymentId) {
        this.orderId = orderId;
        this.status = status;
        this.paymentId = paymentId;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}