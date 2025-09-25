package com.example.FoodOrderApp.email_notification.repository;

import com.example.FoodOrderApp.email_notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
