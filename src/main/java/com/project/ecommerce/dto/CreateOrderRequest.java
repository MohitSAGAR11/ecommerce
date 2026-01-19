package com.project.ecommerce.dto;

public class CreateOrderRequest {
    private String userId;

    // Constructors
    public CreateOrderRequest() {}

    public CreateOrderRequest(String userId) {
        this.userId = userId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}