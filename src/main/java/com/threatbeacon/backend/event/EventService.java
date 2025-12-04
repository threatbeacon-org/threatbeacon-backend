package com.threatbeacon.backend.event;

import com.threatbeacon.backend.api.dto.EventDto;
import com.threatbeacon.backend.incident.IncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final IncidentService incidentService;

    public EventService(EventRepository eventRepository, IncidentService incidentService) {
        this.eventRepository = eventRepository;
        this.incidentService = incidentService;
    }

    @Transactional
    public Event saveEvent(EventDto dto) {
        logger.debug("Saving event with type: {} and severity: {}", dto.getType(), dto.getSeverity());

        // Validate required fields
        validateEventDto(dto);

        // Map DTO to Entity
        Event event = mapDtoToEntity(dto);

        // Persist.
        Event savedEvent = eventRepository.save(event);

        logger.info("Event saved successfully with ID: {}", savedEvent.getId());

        try {
            incidentService.proccesNewEvent(savedEvent);
        } catch (Exception e) {
            logger.error("Error processing incident rules fro event {}: {}", savedEvent.getId(), e.getMessage());
        }
        return savedEvent;
    }

    private void validateEventDto(EventDto dto) {
        if (dto.getType() == null || dto.getType().isBlank()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        if (dto.getSeverity() == null || dto.getSeverity().isBlank()) {
            throw new IllegalArgumentException("Severity cannot be null or empty");
        }
        if (dto.getSource() == null || dto.getSource().isBlank()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
    }

    private Event mapDtoToEntity(EventDto dto) {
        Event event = new Event();
        event.setType(dto.getType());
        event.setSource(dto.getSource());
        event.setIp(dto.getIp());
        event.setCountry(dto.getCountry());
        event.setSeverity(dto.getSeverity());
        event.setMetadata(dto.getMetadata());
        event.setTimestamp(OffsetDateTime.now());

        return event;
    }
}