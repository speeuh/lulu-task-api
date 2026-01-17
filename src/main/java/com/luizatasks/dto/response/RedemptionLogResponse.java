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
public class RedemptionLogResponse {
    private Long id;
    private String itemName;
    private Integer pointsSpent;
    private LocalDateTime redeemedAt;
}

