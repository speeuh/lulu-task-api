package com.luizatasks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLogResponse {
    private Long id;
    private String taskTitle;
    private Integer pointsEarned;
    private LocalDateTime completedAt;
}

