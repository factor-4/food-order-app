package com.example.FoodOrderApp.auth_users.controller;


import com.example.FoodOrderApp.auth_users.dtos.LoginRequest;
import com.example.FoodOrderApp.auth_users.dtos.LoginResponse;
import com.example.FoodOrderApp.auth_users.dtos.RegistrationRequest;
import com.example.FoodOrderApp.auth_users.services.AuthService;
import com.example.FoodOrderApp.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for user registration and login operations."
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account using the provided registration details. " +
                    "The request body must contain valid user information (email, password, etc.)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = Response.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or user already exists",
            content = @Content
    )



    @PostMapping("/register")
    public ResponseEntity<Response<?>> register(@RequestBody @Valid RegistrationRequest registrationRequest){
        return ResponseEntity.ok(authService.register(registrationRequest));
    }


    @Operation(
            summary = "Login user",
            description = "Authenticates a user using email and password. Returns a JWT token on successful login."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials or unauthorized access",
            content = @Content
    )

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest){

        return ResponseEntity.ok(authService.login(loginRequest));
    }


}
