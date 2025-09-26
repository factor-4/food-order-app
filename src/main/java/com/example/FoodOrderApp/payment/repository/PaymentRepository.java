package com.example.FoodOrderApp.payment.repository;

import com.example.FoodOrderApp.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
