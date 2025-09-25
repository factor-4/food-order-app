package com.example.FoodOrderApp.email_notification.dtos;


import com.example.FoodOrderApp.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class NotificationDTO {


    private Long id;

    private String subject;

    @NotBlank(message = "recipient is required")
    private String recipient;


    private String body;

    private NotificationType type;

    private  LocalDateTime createdAt;
    private boolean isHtml;
}
