package com.example.FoodOrderApp.payment.dtos;


import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.enums.PaymentStatus;
import com.example.FoodOrderApp.order.dtos.OrderDTO;
import com.example.FoodOrderApp.enums.PaymentGateway;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO  {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private String transactionId;

    private PaymentGateway paymentGateway;

    private String failureReason;

    private boolean success;

    private LocalDateTime paymentDate;

    private OrderDTO order;
    private UserDTO user;
}
