package com.example.FoodOrderApp.cart.controller;


import com.example.FoodOrderApp.cart.dtos.CartDTO;
import com.example.FoodOrderApp.cart.services.CartService;
import com.example.FoodOrderApp.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(
        name = "Cart Management",
        description = "Endpoints for managing the user's shopping cart — adding, updating, removing, and viewing items."
)
public class CartController {

    private final CartService cartService;


    @Operation(
            summary = "Add an item to the cart",
            description = "Adds a menu item to the user's shopping cart. If the item already exists, it increases the quantity.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item added successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid menu ID or request data", content = @Content)
            }
    )

    @PostMapping("/items")
    public ResponseEntity<Response<?>> addItemToCart(@RequestBody CartDTO cartDTO){
        return ResponseEntity.ok(cartService.addItemToCart(cartDTO));
    }



    @Operation(
            summary = "Increment item quantity",
            description = "Increases the quantity of a specific item in the user's cart by 1.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item quantity incremented successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Menu item not found in cart", content = @Content)
            }
    )

    @PutMapping("/items/increment/{menuId}")
    public ResponseEntity<Response<?>> incrementItem(@PathVariable Long menuId){
        return ResponseEntity.ok(cartService.incrementItem(menuId));
    }



    @Operation(
            summary = "Decrement item quantity",
            description = "Decreases the quantity of a specific item in the user's cart by 1. If quantity becomes 0, the item may be removed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item quantity decremented successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Menu item not found in cart", content = @Content)
            }
    )

    @PutMapping("/items/decrement/{menuId}")
    public ResponseEntity<Response<?>> decrementItem(@PathVariable Long menuId){
        return ResponseEntity.ok(cartService.decrementItem(menuId));
    }



    @Operation(
            summary = "Remove item from cart",
            description = "Removes a specific item from the user's shopping cart based on the cart item ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item removed successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "404", description = "Cart item not found", content = @Content)
            }
    )

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Response<?>> removeItem(@PathVariable Long cartItemId){
        return ResponseEntity.ok(cartService.removeItem(cartItemId));
    }





    @Operation(
            summary = "Get current shopping cart",
            description = "Retrieves the current user's cart, including all items, quantities, and total amount.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CartDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized — user not logged in", content = @Content)
            }
    )


    @GetMapping
    public ResponseEntity<Response<CartDTO>> getShoppingCart(){
        return ResponseEntity.ok(cartService.getShoppingCart());
    }




    @Operation(
            summary = "Clear shopping cart",
            description = "Removes all items from the user's shopping cart.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                            content = @Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized — user not logged in", content = @Content)
            }
    )
    @DeleteMapping
    public ResponseEntity<Response<?>> clearShoppingCart(){
        return ResponseEntity.ok(cartService.clearShoppingCart());
    }


}

