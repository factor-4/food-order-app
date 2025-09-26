package com.example.FoodOrderApp.payment.entity;


import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.enums.PaymentGateway;
import com.example.FoodOrderApp.enums.PaymentStatus;
import com.example.FoodOrderApp.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal amount;



    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    private String failureReason;

    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
