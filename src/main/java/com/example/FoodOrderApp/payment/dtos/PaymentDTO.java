package com.example.FoodOrderApp.payment.dtos;

import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.enums.PaymentStatus;
import com.example.FoodOrderApp.order.dtos.OrderDTO;
import com.example.FoodOrderApp.enums.PaymentGateway;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Payment", description = "Represents payment transaction details associated with an order.")
public class PaymentDTO {

    @Schema(description = "Unique identifier of the payment", example = "501")
    private Long id;

    @Schema(description = "Associated order ID for this payment", example = "1001")
    private Long orderId;

    @Schema(description = "Payment amount in EUR", example = "29.99")
    private BigDecimal amount;

    @Schema(description = "Current status of the payment", example = "SUCCESS")
    private PaymentStatus paymentStatus;

    @Schema(description = "Unique transaction ID returned by payment gateway", example = "TXN_2025_ABC123")
    private String transactionId;

    @Schema(description = "Payment gateway used for the transaction", example = "STRIPE")
    private PaymentGateway paymentGateway;

    @Schema(description = "Reason for payment failure (if any)", example = "Insufficient funds")
    private String failureReason;

    @Schema(description = "Indicates whether payment was successful", example = "true")
    private boolean success;

    @Schema(description = "Timestamp when the payment was made", example = "2025-11-03T18:45:30")
    private LocalDateTime paymentDate;

    @Schema(description = "Associated order details")
    private OrderDTO order;

    @Schema(description = "User who made the payment")
    private UserDTO user;
}
