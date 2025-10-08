package com.example.FoodOrderApp.auth_users.services;

import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.response.Response;

import java.util.List;

public interface UserService {

    User getCurrentLoggedInInUser();

    Response<List<UserDTO>> getAllUsers();

    Response<UserDTO> getOwnAccountDetails();

    Response<?> updatedOwnAccount(UserDTO userDTO);

    Response<?> deactivateOwnAccount();
}
