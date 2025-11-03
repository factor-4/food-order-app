package com.example.FoodOrderApp.category.controller;


import com.example.FoodOrderApp.category.dtos.CategoryDTO;
import com.example.FoodOrderApp.category.services.CategoryService;
import com.example.FoodOrderApp.response.Response;
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
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for creating, updating, and retrieving food categories.")

public class CategoryController {

    private final CategoryService categoryService;


    @Operation(
            summary = "Add a new category",
            description = "Creates a new category. Only ADMIN users can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category successfully created",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input or validation error")
            }
    )

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <Response<CategoryDTO>> addCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }


    @Operation(
            summary = "Update an existing category",
            description = "Updates the details of an existing category. Only ADMIN users can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category successfully updated",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <Response<CategoryDTO>> updateCategory(@RequestBody  CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO));
    }





    @Operation(
            summary = "Get category by ID",
            description = "Fetches category details by its unique identifier.",
            parameters = {
                    @Parameter(name = "id", description = "ID of the category to retrieve", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )


    @GetMapping("/{id}")
    public ResponseEntity <Response<CategoryDTO>> geCategoryById(@PathVariable  Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }



    @Operation(
            summary = "Get all categories",
            description = "Retrieves a list of all available categories.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of categories",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
            }
    )


    @GetMapping("/all")
    public ResponseEntity <Response <List<CategoryDTO>>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }






    @Operation(
            summary = "Delete a category",
            description = "Deletes a category by its ID. Only ADMIN users can perform this action.",
            parameters = {
                    @Parameter(name = "id", description = "ID of the category to delete", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <Response<?>> deleteCategory(@PathVariable  Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
