package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.beacon.BeaconStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Service responsible for calculating and managing the system's risk status.

 * What it does:
 * - Calculates the risk level based on whether the buzzer is muted.
 * - Updates the buzzer mute state and recalculates risk.
 * - Retrieves the current risk status from the beacon state.

 * What it contains:
 * - A reference to BeaconStateService for reading and updating the beacon state.
 * - Logging for debugging, warnings, and operational visibility.
 * - Methods to calculate, update, and fetch risk status.

 * What it needs:
 * - A working implementation of BeaconStateService.
 * - The RiskLevel enum and RiskStatus model.
 * - Spring Boot dependency injection (@Service, @RequiredArgsConstructor).
 * - System clock access to generate timestamps (ZonedDateTime.now()).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RiskService {

    private final BeaconStateService beaconStateService;

//Core function used by the controller to compute risk level
    //based solely on mute state (for now)
    public RiskStatus calculateRiskStatus(Boolean buzzerMuted) {
        if (buzzerMuted == null) {
            log.warn("Null buzzerMuted received -> fallback to NON muted state.");
            buzzerMuted = false;
        }

        RiskLevel level = buzzerMuted ? RiskLevel.NORMAL : RiskLevel.SUSPICIOUS;

        log.info("Risk calculated | level: {}, buzzerMuted: {}", level, buzzerMuted);

        return new RiskStatus(
                level,
                buzzerMuted,
                ZonedDateTime.now()
        );
    }

//High-level service: update mute state and recompute risk.
    //Adaped for extensibility while maintaining controller logic...


    public RiskStatus updateMuteAndRecalculateRisk(Boolean newMutedState) {
        if (newMutedState == null) {
            throw new IllegalArgumentException("Mute state cannot be null.");
        }

        log.debug("Updating buzzer mute state → {}", newMutedState);
        try {
            beaconStateService.setBuzzerMuted(newMutedState);
        } catch (Exception ex) {
            log.error("Error updating buzzer state:", ex);
            throw new RuntimeException("Failed to update buzzer state", ex);
        }

        return calculateRiskStatus(newMutedState);
    }

//Convenience method to retrieve current state of the system risk.
    public RiskStatus getCurrentRiskStatus() {
        boolean currentMuted = beaconStateService.getBeaconState().isBuzzerMuted();
        return calculateRiskStatus(currentMuted);
    }
}