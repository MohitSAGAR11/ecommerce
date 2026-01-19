package com.project.ecommerce.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_items")
@Data
public class OrderItem {

    @Id
    private String id;
    private String orderId;
    private String productId;
    private Integer quantity;
    private Double price; // Price at time of order

    // Constructors
    public OrderItem() {}

    public OrderItem(String orderId, String productId, Integer quantity, Double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

}