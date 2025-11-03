package com.example.FoodOrderApp.role.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Role", description = "Represents a user role in the system.")
public class RoleDTO {

    @Schema(description = "Unique identifier of the role", example = "1")
    private Long id;

    @Schema(description = "Name of the role", example = "ADMIN")
    private String name;
}
