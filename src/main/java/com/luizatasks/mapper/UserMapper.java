package com.luizatasks.mapper;

import com.luizatasks.domain.entity.User;
import com.luizatasks.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .points(user.getPoints())
                .profileImageUrl(user.getProfileImageUrl())
                .themeType(user.getThemeType())
                .themeValue(user.getThemeValue())
                .buttonColor(user.getButtonColor())
                .build();
    }
}

