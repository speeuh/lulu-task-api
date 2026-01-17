package com.luizatasks.dto.request;

import com.luizatasks.domain.enums.ThemeType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserSettingsRequest {
    @NotNull(message = "Theme type is required")
    private ThemeType themeType;
    
    private String themeValue;
    
    private String buttonColor;
}

