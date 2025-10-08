package com.example.FoodOrderApp.auth_users.services;

import com.example.FoodOrderApp.auth_users.dtos.UserDTO;
import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.auth_users.repository.UserRepository;
import com.example.FoodOrderApp.aws.AWSS3Service;
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

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final AWSS3Service awss3Service;
    @Override
    public User getCurrentLoggedInInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        return userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public Response<List<UserDTO>> getAllUsers() {

        log.info("Inside getAllUsers()");
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<UserDTO> userDTOS = modelMapper.map(userList, new TypeToken<List<UserDTO>>(){}.getType());


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

        // check if new image was provided

        if(imageFile != null && !imageFile.isEmpty()){
            // delete old image in cloud if it exists
            if(profileUrl != null && !profileUrl.isEmpty()){
                String keyName = profileUrl.substring(profileUrl.lastIndexOf("/")+1);
                awss3Service.deleteFile("profile/"+ keyName);
                log.info("Deleted old profile image from s3");
            }

            String imageName = UUID.randomUUID().toString()+"-"+imageFile.getOriginalFilename();
            URL newImageUrl = awss3Service.uploadFile("profile/"+ imageName, imageFile);
            user.setProfileUrl(newImageUrl.toString());
        }
        // update user details

        if(userDTO.getName() != null) user.setName(userDTO.getName());
        if(userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());

        if(userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());

        if(userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if(userDTO.getEmail()!= null && !userDTO.getEmail().equals(user.getEmail())){
            if(userRepository.existsByEmail(userDTO.getEmail())){
                throw new BadRequestException("Email already exists");
            }
        }

        user.setEmail(userDTO.getEmail());

        // save the user

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
                .body("Your account has been deactivated, contact suppoert")
                .build();

        notificationService.sendEmail(notificationDTO);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account Deactivated successfully")
                .build();
    }
}
