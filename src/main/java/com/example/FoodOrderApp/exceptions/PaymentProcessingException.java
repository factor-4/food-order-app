package com.example.FoodOrderApp.exceptions;

public class PaymentProcessingException extends RuntimeException{
    public PaymentProcessingException(String message){
        super(message);
    }
}
