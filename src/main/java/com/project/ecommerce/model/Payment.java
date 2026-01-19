package com.project.ecommerce.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "payments")
@Data
public class Payment {

    @Id
    private String id;
    private String orderId;
    private Double amount;
    private String status; // PENDING, SUCCESS, FAILED
    private String paymentId; // External payment ID
    private Instant createdAt;

    // Constructors
    public Payment() {
        this.createdAt = Instant.now();
    }

    public Payment(String orderId, Double amount, String status, String paymentId) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentId = paymentId;
        this.createdAt = Instant.now();
    }

}
