package com.threatbeacon.backend.incident;

import com.threatbeacon.backend.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;


@Service
public class IncidentService {

    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);
    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    /**
     * Entry point: Analyzes the incoming event to see if it triggers an incident
     */
    @Transactional
    public void proccesNewEvent(Event event){
        log.debug("Analyzing events to detect potential incidents: type {}, ip {}", event.getType(), event.getIp());

        // Rule 1: Brute force detection
        if ("LOGIN_FAILED".equals(event.getType())){
            handleBruteForceRule(event);
        }

        // Rule 2: Detecting web error spikes
        if ("HTTP_ERROR".equals(event.getType())){
            handleHttpErrorSpikeRule(event);
        }
    }

    // ------ Rule logic -------
    private void handleBruteForceRule(Event event) {
        // We are checking if there is an OPEN brute force incident
        Optional<Incident> existingIncident = incidentRepository.
                findFirstByStatusAndType(IncidentStatus.OPEN, IncidentType.BRUTE_FORCE_LOGIN);

        if(existingIncident.isPresent()){
            // If the incident exists, we add evidence (we add counter, we add IP)
            updateExistingIncident(existingIncident.get(), event);
        } else {
            // If it DOES NOT EXIST, we create one immediately
            log.warn("BRUTE FORCE ATTACK DETECTED - creating Incident. IP {}", event.getIp());
            createIncident(IncidentType.BRUTE_FORCE_LOGIN, IncidentSeverity.HIGH, event);
        }
    }

    private void handleHttpErrorSpikeRule(Event event) {
        Optional<Incident> existingIncident = incidentRepository
                .findFirstByStatusAndType(IncidentStatus.OPEN, IncidentType.HTTP_ERROR_SPIKE);

        if (existingIncident.isPresent()) {
            updateExistingIncident(existingIncident.get(), event);
        } else {
            log.warn("🚨 HTTP ERROR SPIKE DETECTED - Creating Incident.");
            // Empieza como MEDIUM, si sube mucho el contador podría pasar a HIGH (mejora futura)
            createIncident(IncidentType.HTTP_ERROR_SPIKE, IncidentSeverity.MEDIUM, event);
        }
    }

    private void createIncident(IncidentType type, IncidentSeverity severity, Event triggerEvent) {
        Incident newIncident = Incident.builder()
                .type(type)
                .severity(severity)
                .status(IncidentStatus.OPEN)
                .eventCount(1)
                .mainIps(triggerEvent.getIp())
                .countries(triggerEvent.getCountry())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        incidentRepository.save(newIncident);
    }

    private void updateExistingIncident(Incident incident, Event event) {
        // 1. Increment counter
        incident.setEventCount(incident.getEventCount() + 1);
        incident.setUpdatedAt(Instant.now());

        // 2. Add IP address if it is new and not already on the list (Simple CSV logic)
        String currentIps = incident.getMainIps();
        String newIp = event.getIp();

        if (currentIps != null && newIp != null && !currentIps.contains(newIp)) {
            // We concatenate: "1.1.1.1, 2.2.2.2"
            incident.setMainIps(currentIps + ", " + newIp);
        } else if (currentIps == null) {
            incident.setMainIps(newIp);
        }

        // 3. Add Country if it's new
        String currentCountries = incident.getCountries();
        String newCountry = event.getCountry();

        if (currentCountries != null && newCountry != null && !currentCountries.contains(newCountry)) {
            incident.setCountries(currentCountries + ", " + newCountry);
        } else if (currentCountries == null) {
            incident.setCountries(newCountry);
        }

        incidentRepository.save(incident);
        log.info(" Incident {} updated. Accumulated events: {}", incident.getId(), incident.getEventCount());
    }
}
