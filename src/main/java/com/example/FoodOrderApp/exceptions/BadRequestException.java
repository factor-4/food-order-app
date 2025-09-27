package com.example.FoodOrderApp.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
