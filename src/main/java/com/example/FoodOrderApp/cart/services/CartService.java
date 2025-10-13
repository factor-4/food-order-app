package com.example.FoodOrderApp.cart.services;

import com.example.FoodOrderApp.cart.dtos.CartDTO;
import com.example.FoodOrderApp.response.Response;

public interface CartService {

    Response<?> addItemToCart(CartDTO cartDTO);
    Response<?> incrementItem(Long menuId);
    Response<?> decrementItem(Long menuId);
    Response<?> removeItem(Long cartItemId);
    Response<CartDTO> getShoppingCart();
    Response<?> clearShoppingCart();

}
