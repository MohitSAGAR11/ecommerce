package com.project.ecommerce.service;

import com.project.ecommerce.dto.CreateOrderRequest;
import com.project.ecommerce.model.CartItem;
import com.project.ecommerce.model.Order;
import com.project.ecommerce.model.OrderItem;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        // Get cart items
        List<CartItem> cartItems = cartService.getCartItems(request.getUserId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total and validate stock
        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());

            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found: " + cartItem.getProductId());
            }

            Product product = productOpt.get();

            // Check stock
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Create order item
            OrderItem orderItem = new OrderItem(
                    null, // orderId will be set after order creation
                    product.getId(),
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();

            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Create order
        Order order = new Order(
                request.getUserId(),
                totalAmount,
                "CREATED",
                orderItems
        );
        order = orderRepository.save(order);

        // Clear cart
        cartService.clearCart(request.getUserId());

        return order;
    }

    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(String orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order order = orderOpt.get();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}