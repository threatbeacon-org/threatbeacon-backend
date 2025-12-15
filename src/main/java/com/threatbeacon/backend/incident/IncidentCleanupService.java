package com.threatbeacon.backend.incident;

import com.threatbeacon.backend.beacon.BeaconStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant; // We use Instant to match the Entity
import java.util.List;

@Service
public class IncidentCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(IncidentCleanupService.class);

    private final IncidentRepository incidentRepository;
    private final BeaconStateService beaconStateService;

    // EXPIRATION TIME: 60 seconds without logs = end of the attack
    private static final long ATTACK_TIMEOUT_SECONDS = 35;

    public IncidentCleanupService(IncidentRepository incidentRepository,
                                  BeaconStateService beaconStateService) {
        this.incidentRepository = incidentRepository;
        this.beaconStateService = beaconStateService;
    }

    /**
     * Runs every 2 SECONDS.
     * Heartbeat pattern: Checks if active attacks have stopped sending signals.
     */
    @Scheduled(fixedRate = 2000)
    @Transactional
    public void autoResolveStaleIncidents() {
        // 1. Calculate the cutoff point (60s ago) using Instant
        Instant cutoffTime = Instant.now().minusSeconds(ATTACK_TIMEOUT_SECONDS);

        // 2. Find OPEN incidents that have not been updated since the cutoff
        List<Incident> staleIncidents = incidentRepository.findByStatusAndUpdatedAtBefore(IncidentStatus.OPEN, cutoffTime);

        if (!staleIncidents.isEmpty()) {
            logger.info("⚡ Quick Cleanup: Closing {} inactive incidents.", staleIncidents.size());

            for (Incident incident : staleIncidents) {
                incident.setStatus(IncidentStatus.RESOLVED);
                incident.setUpdatedAt(Instant.now());
            }
            incidentRepository.saveAll(staleIncidents);

            // 3. System Recovery Phase (Buzzer)
            boolean areThereOpenIncidents = incidentRepository.existsByStatus(IncidentStatus.OPEN);

            if (!areThereOpenIncidents) {
                // OPTIMIZATION: We only act if the buzzer is currently muted (Muted = true)
                // This avoids spamming the logs and redundant DB writes every 2 seconds.
                if (beaconStateService.isBuzzerMuted()) {
                    logger.info("Clean and secure system. Resetting Buzzer to active state.");
                    beaconStateService.setBuzzerMuted(false);
                }
            }
        }
    }
}