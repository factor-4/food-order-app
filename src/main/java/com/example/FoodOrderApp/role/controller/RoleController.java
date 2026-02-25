package com.example.FoodOrderApp.role.controller;

import com.example.FoodOrderApp.response.Response;
import com.example.FoodOrderApp.role.dtos.RoleDTO;
import com.example.FoodOrderApp.role.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "Endpoints for managing user roles. Admin-only access.")
//@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "Create a new role",
            description = "Adds a new user role (e.g., ADMIN, CUSTOMER, MANAGER). Only administrators can perform this operation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role created successfully",
                            content = @Content(schema = @Schema(implementation = RoleDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid role data or missing fields")
            }
    )
    @PostMapping
    public ResponseEntity<Response<RoleDTO>> createRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Role details including role name",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoleDTO.class))
            )
            @RequestBody @Valid RoleDTO roleDTO
    ) {
        return ResponseEntity.ok(roleService.createRole(roleDTO));
    }

    @Operation(
            summary = "Update an existing role",
            description = "Modifies the name or attributes of an existing role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role updated successfully",
                            content = @Content(schema = @Schema(implementation = RoleDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Role not found")
            }
    )
    @PutMapping
    public ResponseEntity<Response<RoleDTO>> updateRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated role information",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoleDTO.class))
            )
            @RequestBody @Valid RoleDTO roleDTO
    ) {
        return ResponseEntity.ok(roleService.updateRole(roleDTO));
    }

    @Operation(
            summary = "Get all roles",
            description = "Retrieves all roles defined in the system. Useful for assigning roles to users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of roles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = RoleDTO.class)))
            }
    )
    @GetMapping
    public ResponseEntity<Response<List<RoleDTO>>> getAllRoles() {
        System.out.println("getroles in controller");
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(
            summary = "Delete a role",
            description = "Removes a role by its ID. Be cautious — deleting a role may affect existing users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Role not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRole(
            @Parameter(description = "ID of the role to delete", example = "2")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(roleService.deleteROle(id));
    }
}
