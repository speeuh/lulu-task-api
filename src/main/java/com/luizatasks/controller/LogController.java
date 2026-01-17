package com.luizatasks.controller;

import com.luizatasks.dto.response.RedemptionLogResponse;
import com.luizatasks.dto.response.TaskLogResponse;
import com.luizatasks.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    
    private final LogService logService;
    
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskLogResponse>> getTaskHistory(Authentication authentication) {
        return ResponseEntity.ok(logService.getTaskHistory(authentication));
    }
    
    @GetMapping("/redemptions")
    public ResponseEntity<List<RedemptionLogResponse>> getRedemptionHistory(Authentication authentication) {
        return ResponseEntity.ok(logService.getRedemptionHistory(authentication));
    }
}

