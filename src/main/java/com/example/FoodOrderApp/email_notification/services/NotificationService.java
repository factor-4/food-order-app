package com.example.FoodOrderApp.email_notification.services;

import com.example.FoodOrderApp.email_notification.dtos.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO);
}
