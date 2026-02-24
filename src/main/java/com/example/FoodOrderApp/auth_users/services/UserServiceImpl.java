package com.example.FoodOrderApp.auth_users.services;

import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.auth_users.repository.UserRepository;
import com.example.FoodOrderApp.tempservice.CloudinaryService;
import com.example.FoodOrderApp.email_notification.dtos.NotificationDTO;
import com.example.FoodOrderApp.email_notification.services.NotificationService;
import com.example.FoodOrderApp.exceptions.BadRequestException;
import com.example.FoodOrderApp.exceptions.NotFoundException;
import com.example.FoodOrderApp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final CloudinaryService cloudinaryService;

    // Helper to extract public ID from Cloudinary URL (same as in MenuServiceImpl)
    private String extractPublicIdFromUrl(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 2) {
            String folder = parts[parts.length - 2];
            String file = parts[parts.length - 1].split("\\.")[0];
            return folder + "/" + file;
        }
        return parts[parts.length - 1].split("\\.")[0];
    }

    @Override
    public User getCurrentLoggedInInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Response<List<UserDTO>> getAllUsers() {
        log.info("Inside getAllUsers()");
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDTO> userDTOS = modelMapper.map(userList, new TypeToken<List<UserDTO>>() {}.getType());
        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("All users received successfully")
                .data(userDTOS)
                .build();
    }

    @Override
    public Response<UserDTO> getOwnAccountDetails() {
        log.info("Inside getOwnAccountDetails()");
        User user = getCurrentLoggedInInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("success")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<?> updatedOwnAccount(UserDTO userDTO) {
        log.info("Inside updateOwnAccount()");

        User user = getCurrentLoggedInInUser();
        String profileUrl = user.getProfileUrl();
        MultipartFile imageFile = userDTO.getImageFile();

        // Check if new image was provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old profile image if it exists
            if (profileUrl != null && !profileUrl.isEmpty()) {
                String publicId = extractPublicIdFromUrl(profileUrl);
                try {
                    cloudinaryService.deleteFile(publicId);
                    log.info("Deleted old profile image: {}", publicId);
                } catch (IOException e) {
                    log.error("Failed to delete old profile image: {}", e.getMessage());
                }
            }

            // Upload new profile image
            try {
                String newImageUrl = cloudinaryService.uploadFile(imageFile, "profiles");
                user.setProfileUrl(newImageUrl);
                log.info("New profile image uploaded: {}", newImageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile image", e);
            }
        }

        // Update user details
        if (userDTO.getName() != null) user.setName(userDTO.getName());
        if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());
        if (userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(userDTO.getEmail());
        }

        userRepository.save(user);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account updated successfully")
                .build();
    }

    @Override
    public Response<?> deactivateOwnAccount() {
        log.info("INSIDE deactivateOwnAccount()");

        User user = getCurrentLoggedInInUser();
        user.setActive(false);
        userRepository.save(user);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Account Deactivate")
                .body("Your account has been deactivated, contact support")
                .build();

        notificationService.sendEmail(notificationDTO);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account Deactivated successfully")
                .build();
    }
}