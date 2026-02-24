package com.example.FoodOrderApp.tempservice.controller;

import com.example.FoodOrderApp.tempservice.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "${app.frontend.url:http://localhost:3000}")
@RequiredArgsConstructor
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file, "uploads");
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("message", "File uploaded successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Could not upload file: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}