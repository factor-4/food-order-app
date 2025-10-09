package com.example.FoodOrderApp.auth_users.controller;


import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.auth_users.services.UserService;
import com.example.FoodOrderApp.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<List<UserDTO>>> getAllUsers(){

        return ResponseEntity.ok(userService.getAllUsers());
    }

    
}
