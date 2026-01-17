package com.luizatasks.dto.response;

import com.luizatasks.domain.enums.TaskRecurrence;
import com.luizatasks.domain.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Integer points;
    private TaskStatus status;
    private TaskRecurrence recurrence;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}

