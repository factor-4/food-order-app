package com.example.FoodOrderApp.payment.services;

import com.example.FoodOrderApp.payment.dtos.PaymentDTO;
import com.example.FoodOrderApp.response.Response;

import java.util.List;

public interface PaymentService {

    Response<?> initializePayment(PaymentDTO paymentDTO);
    void updatePaymentForOrder(PaymentDTO paymentDTO);
    Response<List<PaymentDTO>> getAllPayments();
    Response<PaymentDTO> getPaymentById(Long paymentId);

}
