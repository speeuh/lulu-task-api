package com.luizatasks.mapper;

import com.luizatasks.domain.entity.Task;
import com.luizatasks.dto.response.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    
    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .points(task.getPoints())
                .status(task.getStatus())
                .recurrence(task.getRecurrence())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}

