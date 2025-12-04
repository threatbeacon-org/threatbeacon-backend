package com.threatbeacon.backend.api.dto;

import com.threatbeacon.backend.risk.RiskLevel;
import lombok.Builder;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskStatusDto {

    private RiskLevel level;
    private boolean buzzerMuted;
    private ZonedDateTime timestamp;
    private ZonedDateTime updatedAt; //OPTIONAL
}
