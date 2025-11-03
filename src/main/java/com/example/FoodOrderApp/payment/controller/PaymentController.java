package com.example.FoodOrderApp.payment.controller;


import com.example.FoodOrderApp.payment.dtos.PaymentDTO;
import com.example.FoodOrderApp.payment.services.PaymentService;
import com.example.FoodOrderApp.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Tag(name = "Payment Management", description = "Endpoints for initializing payments, updating payment status, and viewing payments.")

public class PaymentController {

    private final PaymentService paymentService;



    @Operation(
            summary = "Initialize a payment",
            description = "Starts the payment process for an order. Returns payment gateway or transaction details based on the integration used.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment initialized successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid payment request or missing fields")
            }
    )

    @PostMapping("/pay")
    public ResponseEntity<Response<?>> initializePayment(@RequestBody @Valid PaymentDTO paymentRequest){
        return ResponseEntity.ok(paymentService.initializePayment(paymentRequest));
    }



    @Operation(
            summary = "Update order after payment",
            description = "Updates the order and payment status after the payment gateway callback or confirmation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment and order updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid payment update data")
            }
    )

    @PutMapping("/update")
    public void updateOrderAfterPayment(@RequestBody PaymentDTO paymentRequest){
        paymentService.updatePaymentForOrder(paymentRequest);
    }



    @Operation(
            summary = "Get all payments (Admin)",
            description = "Retrieves all payment transactions in the system. Only accessible to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payments retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied — only admins can view all payments")
            }
    )

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<List<PaymentDTO>>> getAllPayments(){
        return ResponseEntity.ok(paymentService.getAllPayments());
    }



    @Operation(
            summary = "Get payment by ID",
            description = "Retrieves detailed payment information by its ID. Accessible by the payment’s owner or an admin.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Payment not found")
            }
    )

    @GetMapping("/{paymentId}")
    public ResponseEntity<Response<PaymentDTO>> getPaymentById(@PathVariable Long paymentId){
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

}






