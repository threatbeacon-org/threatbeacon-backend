package com.threatbeacon.backend.event;

import com.threatbeacon.backend.api.dto.EventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event saveEvent(EventDto dto) {
        logger.debug("Saving event with type: {} and severity: {}", dto.getType(), dto.getSeverity());

        // Validate required fields
        validateEventDto(dto);

        // Map DTO to Entity
        Event event = mapDtoToEntity(dto);

        // Persist
        Event savedEvent = eventRepository.save(event);

        logger.info("Event saved successfully with ID: {}", savedEvent.getId());

        return savedEvent;
    }

    private void validateEventDto(EventDto dto) {
        if (dto.getType() == null || dto.getType().isBlank()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        if (dto.getSeverity() == null || dto.getSeverity().isBlank()) {
            throw new IllegalArgumentException("Severity cannot be null or empty");
        }
        if (dto.getTimestamp() == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
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
        event.setTimestamp(dto.getTimestamp());
        event.setMetadata(dto.getMetadata());
        return event;
    }
}