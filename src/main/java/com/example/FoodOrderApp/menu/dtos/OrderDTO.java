package com.example.FoodOrderApp.menu.dtos;


import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.enums.OrderStatus;
import com.example.FoodOrderApp.enums.PaymentStatus;
import com.example.FoodOrderApp.menu.entity.Menu;
import com.example.FoodOrderApp.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderDTO {
    private Long id;



    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private UserDTO user; // Customer who is making/made the order

    private List<OrderItemDTO> orderItems;
}
