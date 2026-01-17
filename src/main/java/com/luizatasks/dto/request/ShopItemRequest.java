package com.luizatasks.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopItemRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Points cost is required")
    @Min(value = 1, message = "Points cost must be at least 1")
    private Integer pointsCost;
    
    private String imageUrl;
}

