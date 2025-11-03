package com.example.FoodOrderApp.menu.controller;


import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.menu.dtos.MenuDTO;
import com.example.FoodOrderApp.menu.services.MenuService;
import com.example.FoodOrderApp.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu Management", description = "Endpoints for creating, updating, deleting, and fetching food menu items.")

public class MenuController {

    private final MenuService menuService;



    @Operation(
            summary = "Create a new menu item",
            description = "Adds a new menu item with name, price, description, and category. Only accessible to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Menu created successfully",
                            content = @Content(schema = @Schema(implementation = MenuDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
            }
    )

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<MenuDTO>> createMenu(
            @ModelAttribute @Valid MenuDTO menuDTO,
            @RequestPart(value = "imageFile", required = true) MultipartFile imageFile
    ){
        menuDTO.setImageFile(imageFile);

        return ResponseEntity.ok(menuService.createMenu(menuDTO));
    }



    @Operation(
            summary = "Update an existing menu item",
            description = "Modifies an existing menu item. The image is optional during update.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Menu updated successfully",
                            content = @Content(schema = @Schema(implementation = MenuDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data or menu not found", content = @Content)
            }
    )

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<MenuDTO>> updateMenu(
            @ModelAttribute @Valid MenuDTO menuDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ){
        menuDTO.setImageFile(imageFile);

        return ResponseEntity.ok(menuService.updateMenu(menuDTO));
    }




    @Operation(
            summary = "Get menu item by ID",
            description = "Retrieves a specific menu item by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Menu item retrieved",
                            content = @Content(schema = @Schema(implementation = MenuDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Menu not found", content = @Content)
            }
    )

    @GetMapping("/{id}")
    public ResponseEntity<Response<MenuDTO>> getMenuById(@PathVariable Long id){
        return ResponseEntity.ok(menuService.getMenuById(id));
    }






    @Operation(
            summary = "Delete a menu item",
            description = "Deletes a menu item by its ID. Only ADMIN users can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Menu deleted successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Menu not found")
            }
    )

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>> deleteMenu(@PathVariable Long id){
        return ResponseEntity.ok(menuService.deleteMenu(id));
    }




    @Operation(
            summary = "List all menu items",
            description = "Retrieves all menu items. Supports optional filtering by category and search term.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of menu items retrieved",
                            content = @Content(schema = @Schema(implementation = MenuDTO.class)))
            }
    )

    @GetMapping
    public ResponseEntity<Response<List<MenuDTO>>> getMenus(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search){

        return ResponseEntity.ok(menuService.getMenus(categoryId, search));

    }
}
