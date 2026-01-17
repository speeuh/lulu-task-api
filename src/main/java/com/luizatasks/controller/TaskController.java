package com.luizatasks.controller;

import com.luizatasks.dto.response.TaskResponse;
import com.luizatasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getUserTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getUserTasks(authentication));
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<TaskResponse>> getPendingTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getPendingTasks(authentication));
    }
    
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            Authentication authentication,
            @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.completeTask(authentication, taskId));
    }
}

