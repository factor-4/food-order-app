package com.example.FoodOrderApp.auth_users.controller;


import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.auth_users.services.UserService;
import com.example.FoodOrderApp.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(
        name = "User Management",
        description = "Operations for viewing, updating, and deactivating user accounts."
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users (Admin only)",
            description = "Retrieves a list of all registered users. Only accessible by administrators.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of users",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied — only admins can view all users",
                            content = @Content
                    )
            }
    )

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<List<UserDTO>>> getAllUsers(){

        return ResponseEntity.ok(userService.getAllUsers());
    }


    @Operation(
            summary = "Update own account",
            description = "Allows a logged-in user to update their personal details and optionally upload a new profile image.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account updated successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content
                    )
            }
    )

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<?>> updateOwnAccount(
            @ModelAttribute  UserDTO userDTO,
            @RequestPart(value = "imageFile", required = false)MultipartFile imageFile
            ){
        userDTO.setImageFile(imageFile);

        return ResponseEntity.ok(userService.updatedOwnAccount(userDTO));
    }



    @Operation(
            summary = "Deactivate own account",
            description = "Allows a logged-in user to deactivate (soft delete) their own account.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account deactivated successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — user not logged in",
                            content = @Content
                    )
            }
    )

    @DeleteMapping("/deactivate")
    public ResponseEntity<Response<?>> deactivateOwnAccount(){

        return ResponseEntity.ok(userService.deactivateOwnAccount());
    }



    @Operation(
            summary = "Get own account details",
            description = "Retrieves the details of the currently logged-in user, including name, email, roles, and profile image.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved user account details",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — user not logged in",
                            content = @Content
                    )
            }
    )

    @GetMapping("/account")
    public ResponseEntity<Response<UserDTO>> getOwnAccountDetails(){

        return ResponseEntity.ok(userService.getOwnAccountDetails());
    }




}
