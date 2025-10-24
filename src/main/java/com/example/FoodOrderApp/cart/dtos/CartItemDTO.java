package com.example.FoodOrderApp.cart.dtos;

import com.example.FoodOrderApp.menu.dtos.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemDTO {


    private Long id;

    private MenuDTO menu;

    private int quantity;

    private BigDecimal pricePerUnit;

    private BigDecimal subtotal;
}
