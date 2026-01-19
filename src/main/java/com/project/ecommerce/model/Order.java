package com.project.ecommerce.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {

    @Id
    private String id;
    private String userId;
    private Double totalAmount;
    private String status; // CREATED, PAID, FAILED, CANCELLED
    private Instant createdAt;
    private List<OrderItem> items;

    // Constructors
    public Order() {
        this.createdAt = Instant.now();
    }

    public Order(String userId, Double totalAmount, String status, List<OrderItem> items) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
        this.createdAt = Instant.now();
    }
}