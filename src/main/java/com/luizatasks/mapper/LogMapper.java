package com.luizatasks.mapper;

import com.luizatasks.domain.entity.RedemptionLog;
import com.luizatasks.domain.entity.TaskLog;
import com.luizatasks.dto.response.RedemptionLogResponse;
import com.luizatasks.dto.response.TaskLogResponse;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {
    
    public TaskLogResponse toTaskLogResponse(TaskLog log) {
        return TaskLogResponse.builder()
                .id(log.getId())
                .taskTitle(log.getTaskTitle())
                .pointsEarned(log.getPointsEarned())
                .completedAt(log.getCompletedAt())
                .build();
    }
    
    public RedemptionLogResponse toRedemptionLogResponse(RedemptionLog log) {
        return RedemptionLogResponse.builder()
                .id(log.getId())
                .itemName(log.getItemName())
                .pointsSpent(log.getPointsSpent())
                .redeemedAt(log.getRedeemedAt())
                .build();
    }
}

