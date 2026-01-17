package com.luizatasks.controller;

import com.luizatasks.dto.request.ShopItemRequest;
import com.luizatasks.dto.request.TaskRequest;
import com.luizatasks.dto.response.ShopItemResponse;
import com.luizatasks.dto.response.TaskResponse;
import com.luizatasks.service.ShopService;
import com.luizatasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final TaskService taskService;
    private final ShopService shopService;
    
    // Debug endpoint
    @GetMapping("/debug")
    public ResponseEntity<String> debug(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok("No authentication found");
        }
        
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        
        String debug = String.format(
            "User: %s\nAuthorities: %s\nAuthenticated: %s",
            authentication.getName(),
            authorities,
            authentication.isAuthenticated()
        );
        
        System.out.println("=== Debug Endpoint Called ===");
        System.out.println(debug);
        System.out.println("============================");
        
        return ResponseEntity.ok(debug);
    }
    
    // Task Management
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        System.out.println("AdminController.getAllTasks() called");
        return ResponseEntity.ok(taskService.getAllActiveTasks());
    }
    
    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        System.out.println("AdminController.createTask() called with request: " + request);
        return ResponseEntity.ok(taskService.createTask(request));
    }
    
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }
    
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
    
    // Shop Item Management
    @GetMapping("/shop/items")
    public ResponseEntity<List<ShopItemResponse>> getAllItems() {
        return ResponseEntity.ok(shopService.getAllItems());
    }
    
    @PostMapping("/shop/items")
    public ResponseEntity<ShopItemResponse> createItem(@Valid @RequestBody ShopItemRequest request) {
        return ResponseEntity.ok(shopService.createItem(request));
    }
    
    @PutMapping("/shop/items/{itemId}")
    public ResponseEntity<ShopItemResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ShopItemRequest request) {
        return ResponseEntity.ok(shopService.updateItem(itemId, request));
    }
    
    @DeleteMapping("/shop/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        shopService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}

