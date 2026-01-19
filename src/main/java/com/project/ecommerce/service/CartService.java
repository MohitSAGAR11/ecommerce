package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddToCartRequest;
import com.project.ecommerce.model.CartItem;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItem addToCart(AddToCartRequest request) {
        // Validate product exists
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Product product = productOpt.get();

        // Check stock availability
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Check if item already in cart
        Optional<CartItem> existingCartItem = cartRepository.findByUserIdAndProductId(
                request.getUserId(),
                request.getProductId()
        );

        if (existingCartItem.isPresent()) {
            // Update quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            return cartRepository.save(cartItem);
        } else {
            // Add new item
            CartItem cartItem = new CartItem(
                    request.getUserId(),
                    request.getProductId(),
                    request.getQuantity()
            );
            return cartRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartItems(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}