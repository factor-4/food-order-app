package com.example.FoodOrderApp.order.repository;

import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.enums.OrderStatus;
import com.example.FoodOrderApp.order.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    List<Order> findByUserOrderByOrderDateDesc(User user);

    @Query("SELECT COUNT(DISTINCT o.user.id) From Order o")
    long countDistinctUsers();
}
