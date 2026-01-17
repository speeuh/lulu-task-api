package com.luizatasks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopItemResponse {
    private Long id;
    private String name;
    private String description;
    private Integer pointsCost;
    private String imageUrl;
    private Boolean available;
}

