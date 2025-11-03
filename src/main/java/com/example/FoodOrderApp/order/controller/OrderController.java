package com.example.FoodOrderApp.order.controller;


import com.example.FoodOrderApp.enums.OrderStatus;
import com.example.FoodOrderApp.order.dtos.OrderDTO;
import com.example.FoodOrderApp.order.dtos.OrderItemDTO;
import com.example.FoodOrderApp.order.services.OrderService;
import com.example.FoodOrderApp.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Endpoints for placing orders, retrieving user orders, and managing order statuses.")

public class OrderController {

    private final OrderService orderService;


    @Operation(
            summary = "Checkout and place an order",
            description = "Places an order using the items currently in the customer's cart. Accessible only to users with the CUSTOMER role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order placed successfully"),
                    @ApiResponse(responseCode = "400", description = "Cart is empty or invalid"),
                    @ApiResponse(responseCode = "403", description = "Access denied — only customers can place orders")
            }
    )

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Response<?>> checkout(){
        return ResponseEntity.ok(orderService.placeOrderFromCart());
    }




    @Operation(
            summary = "Get order by ID",
            description = "Retrieves the details of a specific order by its ID. Accessible to customers for their own orders or admins for all orders.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order details retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDTO>> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }




    @Operation(
            summary = "Get current user's orders",
            description = "Retrieves a list of all orders placed by the currently authenticated customer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class)))
            }
    )


    @GetMapping("/me")
    public ResponseEntity<Response<List<OrderDTO>>> getMyOrders(){
        return ResponseEntity.ok(orderService.getOrdersOfUser());
    }




    @Operation(
            summary = "Get specific order item details",
            description = "Retrieves detailed information about a specific order item using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order item retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderItemDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Order item not found")
            }
    )

    @GetMapping("/order-item/{orderItemId}")
    public ResponseEntity<Response<OrderItemDTO>> getOrderItemById(@PathVariable Long orderItemId) {
        return ResponseEntity.ok(orderService.getOrderItemById(orderItemId));
    }



    @Operation(
            summary = "Get all orders (Admin)",
            description = "Retrieves all customer orders with optional filtering by order status. Supports pagination.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied — only admins can view all orders")
            }
    )


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<Page<OrderDTO>>> getAllOrders(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(orderStatus, page, size));
    }



    @Operation(
            summary = "Update order status (Admin)",
            description = "Updates the status of an order (e.g., from PENDING to DELIVERED). Only accessible to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            }
    )


    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<OrderDTO>> updateOrderStatus(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderDTO));
    }




    @Operation(
            summary = "Count unique customers (Admin)",
            description = "Returns the total number of unique customers who have placed at least one order. Admin only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            }
    )


    @GetMapping("/unique-customers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<Long>> countUniqueCustomers() {
        return ResponseEntity.ok(orderService.countUniqueCustomers());
    }


}
