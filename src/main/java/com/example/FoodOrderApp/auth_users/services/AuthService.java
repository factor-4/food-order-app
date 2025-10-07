package com.example.FoodOrderApp.auth_users.services;

import com.example.FoodOrderApp.auth_users.dtos.LoginRequest;
import com.example.FoodOrderApp.auth_users.dtos.LoginResponse;
import com.example.FoodOrderApp.auth_users.dtos.RegistrationRequest;
import com.example.FoodOrderApp.response.Response;

public interface AuthService {

    Response<?> register(RegistrationRequest registrationRequest);
    Response<LoginResponse> login(LoginRequest loginRequest);

}
