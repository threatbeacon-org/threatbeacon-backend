package com.threatbeacon.backend.risk;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class RiskStatus {
    private RiskLevel level;
    private boolean buzzerMuted;
    private ZonedDateTime updatedAt;
}

