package com.example.FoodOrderApp.cart.repository;

import com.example.FoodOrderApp.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}