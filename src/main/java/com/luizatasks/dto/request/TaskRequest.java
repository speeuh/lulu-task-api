package com.luizatasks.dto.request;

import com.luizatasks.domain.enums.TaskRecurrence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Points are required")
    @Min(value = 1, message = "Points must be at least 1")
    private Integer points;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private TaskRecurrence recurrence = TaskRecurrence.NONE;
}

