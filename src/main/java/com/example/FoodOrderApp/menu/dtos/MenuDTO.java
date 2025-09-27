package com.example.FoodOrderApp.menu.dtos;

import com.example.FoodOrderApp.category.entity.Category;
import com.example.FoodOrderApp.review.dtos.ReviewDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDTO {

    private Long id;

    @NotBlank(message = "Name is Required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    private String imageUrl;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private MultipartFile imageFile;

    private List<ReviewDTO> reviews;
}
