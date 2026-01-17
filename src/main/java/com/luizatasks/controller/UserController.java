package com.luizatasks.controller;

import com.luizatasks.dto.request.UpdateUserSettingsRequest;
import com.luizatasks.dto.response.UserResponse;
import com.luizatasks.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication));
    }
    
    @PutMapping("/profile-image")
    public ResponseEntity<UserResponse> updateProfileImage(
            Authentication authentication,
            @RequestBody String imageUrl) {
        return ResponseEntity.ok(userService.updateProfileImage(authentication, imageUrl));
    }
    
    @PutMapping("/settings")
    public ResponseEntity<UserResponse> updateSettings(
            Authentication authentication,
            @Valid @RequestBody UpdateUserSettingsRequest request) {
        return ResponseEntity.ok(userService.updateSettings(authentication, request));
    }
    
    @PutMapping("/name")
    public ResponseEntity<UserResponse> updateName(
            Authentication authentication,
            @RequestBody String fullName) {
        return ResponseEntity.ok(userService.updateName(authentication, fullName));
    }
}

