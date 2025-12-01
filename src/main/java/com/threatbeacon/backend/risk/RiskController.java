package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.beacon.BeaconStateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;
    private final BeaconStateService beaconStateService;

    // We use constructor injection for dependencies (best practice)
    public RiskController(RiskService riskService, BeaconStateService beaconStateService) {
        this.riskService = riskService;
        this.beaconStateService = beaconStateService;
    }

    /**
     * GET /api/risk
     * Retrieves the current global risk level and the buzzer status.
     * @return RiskStatusDto containing level, muted status, and timestamp.
     */
    @GetMapping
    public ResponseEntity<RiskStatusDto> getGlobalRiskStatus() {
        // --- TEMPORARY IMPLEMENTATION FOR SPRINT 1 (T2.4.4) ---
        // In S1, we don't have the RiskService fully implemented,
        // so we return a temporary status to allow frontend/IoT development to proceed.

        // Get the current persistent mute status from the BeaconStateService
        boolean isMuted = beaconStateService.getBeaconState().isBuzzerMuted();

        // In Sprint 1, we simulate a SUSPICIOUS state if not muted,
        // or a NORMAL state if muted, just for testing the structure.
        RiskLevel currentLevel = isMuted ? RiskLevel.NORMAL : RiskLevel.SUSPICIOUS;

        RiskStatusDto dto = RiskStatusDto.builder()
                .level(currentLevel)
                .buzzerMuted(isMuted)
                .updatedAt(ZonedDateTime.now())
                .build();

        // This will be replaced by riskService.calculateRiskStatus() in Sprint 2.

        return ResponseEntity.ok(dto);
    }
}
