package com.luizatasks.controller;

import com.luizatasks.dto.response.ShopItemResponse;
import com.luizatasks.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {
    
    private final ShopService shopService;
    
    @GetMapping("/items")
    public ResponseEntity<List<ShopItemResponse>> getAvailableItems() {
        return ResponseEntity.ok(shopService.getAvailableItems());
    }
    
    @PostMapping("/items/{itemId}/redeem")
    public ResponseEntity<ShopItemResponse> redeemItem(
            Authentication authentication,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(shopService.redeemItem(authentication, itemId));
    }
}

