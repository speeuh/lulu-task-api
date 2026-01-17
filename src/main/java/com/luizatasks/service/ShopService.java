package com.luizatasks.service;

import com.luizatasks.domain.entity.RedemptionLog;
import com.luizatasks.domain.entity.ShopItem;
import com.luizatasks.domain.entity.User;
import com.luizatasks.dto.request.ShopItemRequest;
import com.luizatasks.dto.response.ShopItemResponse;
import com.luizatasks.mapper.ShopItemMapper;
import com.luizatasks.repository.RedemptionLogRepository;
import com.luizatasks.repository.ShopItemRepository;
import com.luizatasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {
    
    private final ShopItemRepository shopItemRepository;
    private final UserRepository userRepository;
    private final RedemptionLogRepository redemptionLogRepository;
    private final ShopItemMapper shopItemMapper;
    
    @Transactional(readOnly = true)
    public List<ShopItemResponse> getAvailableItems() {
        return shopItemRepository.findByAvailableTrue().stream()
                .map(shopItemMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ShopItemResponse> getAllItems() {
        return shopItemRepository.findAll().stream()
                .map(shopItemMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ShopItemResponse redeemItem(Authentication authentication, Long itemId) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ShopItem item = shopItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        if (!item.getAvailable()) {
            throw new RuntimeException("Item is not available");
        }
        
        if (user.getPoints() < item.getPointsCost()) {
            throw new RuntimeException("Insufficient points");
        }
        
        // Deduct points from user
        user.setPoints(user.getPoints() - item.getPointsCost());
        userRepository.save(user);
        
        // Create immutable redemption log
        RedemptionLog log = RedemptionLog.builder()
                .shopItem(item)
                .user(user)
                .itemName(item.getName())
                .pointsSpent(item.getPointsCost())
                .build();
        redemptionLogRepository.save(log);
        
        return shopItemMapper.toResponse(item);
    }
    
    @Transactional
    public ShopItemResponse createItem(ShopItemRequest request) {
        ShopItem item = ShopItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .pointsCost(request.getPointsCost())
                .imageUrl(request.getImageUrl())
                .available(true)
                .build();
        
        item = shopItemRepository.save(item);
        return shopItemMapper.toResponse(item);
    }
    
    @Transactional
    public ShopItemResponse updateItem(Long itemId, ShopItemRequest request) {
        ShopItem item = shopItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPointsCost(request.getPointsCost());
        item.setImageUrl(request.getImageUrl());
        
        item = shopItemRepository.save(item);
        return shopItemMapper.toResponse(item);
    }
    
    @Transactional
    public void deleteItem(Long itemId) {
        ShopItem item = shopItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setAvailable(false);
        shopItemRepository.save(item);
    }
}

