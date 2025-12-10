package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.beacon.BeaconStateService;
import com.threatbeacon.backend.incident.Incident;
import com.threatbeacon.backend.incident.IncidentRepository;
import com.threatbeacon.backend.incident.IncidentSeverity;
import com.threatbeacon.backend.incident.IncidentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskService {

    private final BeaconStateService beaconStateService;
    private final IncidentRepository incidentRepository;

    public RiskStatus calculateRiskStatus(Boolean buzzerMuted) {
        if (buzzerMuted == null) {
            log.warn("Null buzzerMuted received -> fallback to NON muted state.");
            buzzerMuted = false;
        }

        List<Incident> activeIncidents = incidentRepository.findAllByStatus(IncidentStatus.OPEN);
        RiskLevel level = calculateRiskLevel(activeIncidents);

        if (level == RiskLevel.NORMAL) {
            beaconStateService.setBuzzerMuted(false);
            buzzerMuted = false;
        }

        log.info("Risk calculated | level: {}, buzzerMuted: {}", level, buzzerMuted);

        return new RiskStatus(
                level,
                buzzerMuted,
                ZonedDateTime.now()
        );
    }

    private RiskLevel calculateRiskLevel(List<Incident> activeIncidents) {
        if (activeIncidents.isEmpty()) {
            return RiskLevel.NORMAL;
        }

        boolean hasHighOrCritical = activeIncidents.stream()
                .anyMatch(incident -> incident.getSeverity() == IncidentSeverity.HIGH || incident.getSeverity() == IncidentSeverity.CRITICAL);

        if (hasHighOrCritical) {
            return RiskLevel.CRITICAL;
        }

        return RiskLevel.SUSPICIOUS;
    }

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

    public RiskStatus getCurrentRiskStatus() {
        boolean currentMuted = beaconStateService.getBeaconState().isBuzzerMuted();
        return calculateRiskStatus(currentMuted);
    }
}
