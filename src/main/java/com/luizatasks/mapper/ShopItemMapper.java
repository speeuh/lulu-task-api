package com.luizatasks.mapper;

import com.luizatasks.domain.entity.ShopItem;
import com.luizatasks.dto.response.ShopItemResponse;
import org.springframework.stereotype.Component;

@Component
public class ShopItemMapper {
    
    public ShopItemResponse toResponse(ShopItem item) {
        return ShopItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .pointsCost(item.getPointsCost())
                .imageUrl(item.getImageUrl())
                .available(item.getAvailable())
                .build();
    }
}

