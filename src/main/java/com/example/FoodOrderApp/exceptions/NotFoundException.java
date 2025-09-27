package com.example.FoodOrderApp.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
