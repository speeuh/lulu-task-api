package com.luizatasks.dto.response;

import com.luizatasks.domain.enums.ThemeType;
import com.luizatasks.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private UserRole role;
    private Integer points;
    private String profileImageUrl;
    private ThemeType themeType;
    private String themeValue;
    private String buttonColor;
}

