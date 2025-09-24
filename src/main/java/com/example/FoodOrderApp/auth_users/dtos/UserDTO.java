package com.example.FoodOrderApp.auth_users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;
    private String name;
    private String phoneNumber;

    private String profileUrl;

    // write only: Included when receiving data, excluded when sending data
    // only used for writing
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean isActive;

    private String address;
    private Lis<RoleDTO> roles;

    private MultipartFile imageFile;
}
