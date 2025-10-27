package com.example.FoodOrderApp.debug;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @Autowired(required = false)
    private OpenAPI openAPI; // will be null if your OpenAPI bean or springdoc not present

    @GetMapping("/debug/openapi-bean")
    public Object openapiBean() {
        return openAPI == null ? "NO_OPENAPI_BEAN" : openAPI;
    }
}
