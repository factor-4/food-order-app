package com.example.FoodOrderApp.review.controller;


import com.example.FoodOrderApp.response.Response;
import com.example.FoodOrderApp.review.dtos.ReviewDTO;
import com.example.FoodOrderApp.review.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "Endpoints for adding and retrieving reviews for menu items.")

public class ReviewController {

    private final ReviewService reviewService;




    @Operation(
            summary = "Create a review for a menu item",
            description = "Allows an authenticated customer to submit a rating and comment for a specific menu item they’ve ordered.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review created successfully",
                            content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input or missing fields")
            }
    )

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<ReviewDTO>> createReview(
            @RequestBody @Valid ReviewDTO reviewDTO
    ){
        return ResponseEntity.ok(reviewService.createReview(reviewDTO));
    }




    @Operation(
            summary = "Get all reviews for a specific menu item",
            description = "Retrieves all reviews posted by customers for the given menu item.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Menu item not found or has no reviews")
            }
    )
    @GetMapping("/menu-item/{menuId}")
    public ResponseEntity<Response<List<ReviewDTO>>> getReviewsForMenu(
            @PathVariable Long menuId) {
        return ResponseEntity.ok(reviewService.getReviewsForMenu(menuId));
    }



    @Operation(
            summary = "Get average rating for a menu item",
            description = "Calculates the average customer rating (1–10) for a specific menu item based on submitted reviews.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Average rating calculated successfully",
                            content = @Content(schema = @Schema(example = "8.5"))),
                    @ApiResponse(responseCode = "404", description = "Menu item not found")
            }
    )

    @GetMapping("/menu-item/average/{menuId}")
    public ResponseEntity<Response<Double>> getAverageRating(
            @PathVariable Long menuId) {
        return ResponseEntity.ok(reviewService.getAverageRating(menuId));
    }

}
