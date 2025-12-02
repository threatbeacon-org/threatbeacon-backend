package com.threatbeacon.backend.risk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

//Connection with Mapper and Controller


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskStatus {
    private RiskLevel level;
    private boolean buzzerMuted;
    private ZonedDateTime timestamp;
}

