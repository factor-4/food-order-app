package com.example.FoodOrderApp.order.dtos;

import com.example.FoodOrderApp.menu.dtos.MenuDTO;
import com.example.FoodOrderApp.menu.entity.Menu;
import com.example.FoodOrderApp.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDTO {

    private Long id;


    private int quantity;
    private Long menuId;

    private MenuDTO menu;

    private BigDecimal pricePerUnit;

    private BigDecimal subtotal;
}
