package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import org.springframework.stereotype.Service;

/**
 * Core service responsible for calculating the global risk level based on active incidents.
 * NOTE: In Sprint 1, this class is just a placeholder (stub). The real logic is implemented in T2.4.3.
 */
@Service
public class RiskService {

    // The real implementation will receive dependencies (IncidentRepository, BeaconStateService)

    /**
     * Calculates the current global risk status.
     * Temporary stub method for Sprint 1.
     */
    public RiskStatusDto calculateRiskStatus(boolean isMuted) {



        // This method will return the real RiskStatusDto in Sprint 2..
        throw new UnsupportedOperationException("RiskService logic not yet implemented. Use RiskController for testing in S1..");
    }
}