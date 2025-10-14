package com.example.FoodOrderApp.cart.services;

import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.auth_users.services.UserService;
import com.example.FoodOrderApp.cart.dtos.CartDTO;
import com.example.FoodOrderApp.cart.entity.Cart;
import com.example.FoodOrderApp.cart.entity.CartItem;
import com.example.FoodOrderApp.cart.repository.CartItemRepository;
import com.example.FoodOrderApp.cart.repository.CartRepository;
import com.example.FoodOrderApp.exceptions.NotFoundException;
import com.example.FoodOrderApp.menu.entity.Menu;
import com.example.FoodOrderApp.menu.repository.MenuRepository;
import com.example.FoodOrderApp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        log.info("Inside addItemToCart()");
        Long menuId = cartDTO.getMenuId();
        int quantity = cartDTO.getQuantity();

        User user = userService.getCurrentLoggedInInUser();

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new NotFoundException("Menu Item not found"));

        Cart cart = cartRepository.findByUser_id(user.getId())
                .orElseGet(()-> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems( new ArrayList<>());
                    return  cartRepository.save(newCart);
                });

        // check if the item is already in the cart

        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getMenu().getId().equals(menuId))
                .findFirst();

        // If present, increment item
        if(optionalCartItem.isPresent()){
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setSubTotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        } else {
            // if not present add
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .menu(menu)
                    .quantity(quantity)
                    .pricePerUnit(menu.getPrice())
                    .subTotal(menu.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();

            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        return  Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item added successfully")
                .build();


    }

    @Override
    public Response<?> incrementItem(Long menuId) {
        log.info("Inside incrementItem()");
        User user = userService.getCurrentLoggedInInUser();

        Cart cart = cartRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart Not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item-> item.getMenu().getId().equals(menuId))
                .findFirst().orElseThrow(()-> new NotFoundException("Menu not found in cart"));


        int newQuantity = cartItem.getQuantity() + 1;

        cartItem.setQuantity(newQuantity);

        cartItem.setSubTotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(newQuantity)));
        cartItemRepository.save(cartItem);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity incremented successfully")
                .build();


    }

    @Override
    public Response<?> decrementItem(Long menuId) {
        log.info("Inside decrementItem()");
        User user = userService.getCurrentLoggedInInUser();

        Cart cart = cartRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart Not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item-> item.getMenu().getId().equals(menuId))
                .findFirst().orElseThrow(()-> new NotFoundException("Menu not found in cart"));


        int newQuantity = cartItem.getQuantity() - 1;

        if(newQuantity > 0) {
            cartItem.setQuantity(newQuantity);
            cartItem.setSubTotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(newQuantity)));
            cartItemRepository.save(cartItem);
        } else {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity updated successfully")
                .build();
    }

    @Override
    public Response<?> removeItem(Long cartItemId) {
        log.info("Inside removeItem()");

        User user = userService.getCurrentLoggedInInUser();

        Cart cart = cartRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart Not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new NotFoundException("Cart item not found"));

        if(!cart.getCartItems().contains(cartItem)){
            throw new NotFoundException("Cart item does not belong to this user cart");

        }
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item removed from cart  successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response<CartDTO> getShoppingCart() {
        log.info("Inside getShoppingCart()");
        User user = userService.getCurrentLoggedInInUser();

        Cart cart = cartRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart Not found for user"));

        List<CartItem> cartItems = cart.getCartItems();

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        // calculat total amount

        BigDecimal totalAmount = BigDecimal.ZERO;
        if(cartItems!= null) {
            for(CartItem item :  cartItems) {
                totalAmount = totalAmount.add(item.getSubTotal());
            }
        }

        cartDTO.setTotalAmount(totalAmount);

        // remove the review from the response

        if(cartDTO.getCartItem() != null){
            cartDTO.getCartItem()
                    .forEach( item->item.getMenu().setReviews(null));
        }

        return Response.<CartDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Shopping cart retrieved successfully")
                .data(cartDTO)
                .build();
    }

    @Override
    public Response<?> clearShoppingCart() {
        log.info("Inside clearShoppingCart()");
        User user = userService.getCurrentLoggedInInUser();

        Cart cart = cartRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart Not found for user"));


        //Delete cart items from database first
        cartItemRepository.deleteAll(cart.getCartItems());

        // clear the carts items collection
        cart.getCartItems().clear();

        //update the database
        cartRepository.save(cart);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Shopping cart cleared successfully")
                .build();
    }
}
