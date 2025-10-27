package com.example.FoodOrderApp;

import com.example.FoodOrderApp.email_notification.dtos.NotificationDTO;
import com.example.FoodOrderApp.email_notification.services.NotificationService;
import com.example.FoodOrderApp.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@RequiredArgsConstructor




public class FoodOrderAppApplication {

	public static void main(String[] args) {

        SpringApplication.run(FoodOrderAppApplication.class, args
        );
	}



}
