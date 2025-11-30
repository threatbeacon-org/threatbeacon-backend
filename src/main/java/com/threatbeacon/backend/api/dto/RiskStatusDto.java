package com.threatbeacon.backend.api.dto;

import com.threatbeacon.backend.risk.RiskLevel;
import com.threatbeacon.backend.risk.RiskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class RiskStatusDto extends RiskStatus {

    /**
     * The current global risk level (NORMAL, SUSPICIOUS, CRITICAL).
     */
    private final RiskLevel level;

    /**
     * Flag indicating whether the physical buzzer is currently muted by an analyst.
     * The beacon must check this flag.
     */
    private final boolean buzzerMuted;

    /**
     * Timestamp of the last time the risk status was calculated or updated.
     */
    private final ZonedDateTime updatedAt;
}
