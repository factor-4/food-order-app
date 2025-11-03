package com.example.FoodOrderApp.review.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Review", description = "Represents a customer review for a menu item.")
public class ReviewDTO {

    @Schema(description = "Unique identifier of the review", example = "301")
    private Long id;

    @Schema(description = "ID of the menu item being reviewed", example = "12")
    private Long menuId;

    @Schema(description = "Order ID associated with this review", example = "502")
    private Long orderId;

    @Schema(description = "Username of the reviewer", example = "john_doe")
    private String userName;

    @Schema(description = "Rating score between 1 and 10", example = "8")
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rating;

    @Schema(description = "Optional text comment about the menu item", example = "Loved the taste and portion size!")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;

    @Schema(description = "Name of the menu item being reviewed", example = "Chicken Alfredo Pasta")
    private String menuName;

    @Schema(description = "Timestamp when the review was created", example = "2025-11-03T19:42:15")
    private LocalDateTime createdAt;
}
