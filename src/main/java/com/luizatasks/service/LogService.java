package com.luizatasks.service;

import com.luizatasks.domain.entity.User;
import com.luizatasks.dto.response.RedemptionLogResponse;
import com.luizatasks.dto.response.TaskLogResponse;
import com.luizatasks.mapper.LogMapper;
import com.luizatasks.repository.RedemptionLogRepository;
import com.luizatasks.repository.TaskLogRepository;
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
public class LogService {
    
    private final TaskLogRepository taskLogRepository;
    private final RedemptionLogRepository redemptionLogRepository;
    private final UserRepository userRepository;
    private final LogMapper logMapper;
    
    @Transactional(readOnly = true)
    public List<TaskLogResponse> getTaskHistory(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return taskLogRepository.findByUserOrderByCompletedAtDesc(user).stream()
                .map(logMapper::toTaskLogResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RedemptionLogResponse> getRedemptionHistory(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return redemptionLogRepository.findByUserOrderByRedeemedAtDesc(user).stream()
                .map(logMapper::toRedemptionLogResponse)
                .collect(Collectors.toList());
    }
}

