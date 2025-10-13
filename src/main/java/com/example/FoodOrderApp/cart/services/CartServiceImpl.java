package com.example.FoodOrderApp.cart.services;

import com.example.FoodOrderApp.auth_users.services.UserService;
import com.example.FoodOrderApp.cart.dtos.CartDTO;
import com.example.FoodOrderApp.cart.repository.CartItemRepository;
import com.example.FoodOrderApp.cart.repository.CartRepository;
import com.example.FoodOrderApp.menu.repository.MenuRepository;
import com.example.FoodOrderApp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl  implements CartService{

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Override
    public Response<?> addItemToCart(CartDTO cartDTO) {
        return null;
    }

    @Override
    public Response<?> incrementItem(Long menuId) {
        return null;
    }

    @Override
    public Response<?> decrementItem(Long menuId) {
        return null;
    }

    @Override
    public Response<?> removeItem(Long cartItemId) {
        return null;
    }

    @Override
    public Response<CartDTO> getShoppingCart() {
        return null;
    }

    @Override
    public Response<?> clearShoppingCart() {
        return null;
    }
}
