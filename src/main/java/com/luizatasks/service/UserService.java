package com.luizatasks.service;

import com.luizatasks.domain.entity.User;
import com.luizatasks.dto.request.UpdateUserSettingsRequest;
import com.luizatasks.dto.response.UserResponse;
import com.luizatasks.mapper.UserMapper;
import com.luizatasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }
    
    @Transactional
    public UserResponse updateProfileImage(Authentication authentication, String imageUrl) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setProfileImageUrl(imageUrl);
        user = userRepository.save(user);
        
        return userMapper.toResponse(user);
    }
    
    @Transactional
    public UserResponse updateSettings(Authentication authentication, UpdateUserSettingsRequest request) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setThemeType(request.getThemeType());
        user.setThemeValue(request.getThemeValue());
        
        if (request.getButtonColor() != null) {
            user.setButtonColor(request.getButtonColor());
        }
        
        user = userRepository.save(user);
        
        return userMapper.toResponse(user);
    }
    
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Transactional
    public UserResponse updateName(Authentication authentication, String fullName) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFullName(fullName);
        user = userRepository.save(user);
        
        return userMapper.toResponse(user);
    }
}

