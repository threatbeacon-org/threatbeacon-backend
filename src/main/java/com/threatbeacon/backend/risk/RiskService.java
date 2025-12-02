package com.threatbeacon.backend.risk;

// We import the DOMAIN object, not the DTO
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

@Service
public class RiskService {

    // Actual dependencies (Repositories) will be injected here in Sprint 2

    /**
     * Calculates global risk status.
     * * FIX: Now returns 'RiskStatus' (Domain) instead of 'RiskStatusDto'.
     * This allows the Controller to use the Mapper correctly.
     */
    public RiskStatus calculateRiskStatus(boolean buzzerMuted) {
        // Temporal logic for Sprint 1 (Simulation)
        // If muted -> NORMAL (Green)
        // If not muted -> SUSPICIOUS (Orange) - To test what changes
        RiskLevel level = buzzerMuted ? RiskLevel.NORMAL : RiskLevel.SUSPICIOUS;

        // Return the domain object
        return new RiskStatus(
                level,
                buzzerMuted,
                ZonedDateTime.now()
        );
    }
}