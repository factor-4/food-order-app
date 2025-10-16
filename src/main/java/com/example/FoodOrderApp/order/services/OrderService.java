package com.example.FoodOrderApp.order.services;

import com.example.FoodOrderApp.enums.OrderStatus;
import com.example.FoodOrderApp.order.dtos.OrderDTO;
import com.example.FoodOrderApp.order.dtos.OrderItemDTO;
import com.example.FoodOrderApp.response.Response;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Response<?> placeOrderFromCart();
    Response<OrderDTO> getOrderById(Long id);
    Response<Page<OrderDTO>> getAllOrders(OrderStatus orderStatus, int page, int size);
    Response<List<OrderDTO>> getOrdersOfUser();
    Response<OrderItemDTO> getOrderItemById(Long orderItemId);
    Response<OrderDTO> updateOrderStatus(OrderDTO orderDTO);
    Response<Long> countUniqueCustomers();
}
