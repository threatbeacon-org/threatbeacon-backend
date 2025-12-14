package com.threatbeacon.backend.event;

import com.threatbeacon.backend.api.dto.EventDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@Slf4j // Add logging capabilities
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<String> ingestEvent(@Valid @RequestBody EventDto eventDto){
        log.info("Received request to ingest event. Payload: {}", eventDto);

        try {
            log.info("Calling EventService to save the event...");
            eventService.saveEvent(eventDto);
            log.info("Event successfully processed for IP: {}", eventDto.getIp());
            return ResponseEntity.status(HttpStatus.CREATED).body("Event received and processed");
        } catch (Exception e) {
            log.error("!!! FAILED to process event. Payload: {}. Error: {}", eventDto, e.getMessage(), e);
            // Return a generic 500 error to the client without exposing internal details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process event.");
        }
    }
}
