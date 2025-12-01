package com.threatbeacon.backend.event;

import com.threatbeacon.backend.api.dto.EventDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<String> ingestEvent(@Valid @RequestBody EventDto eventDto){
        eventService.saveEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event received and processed");
    }
}
