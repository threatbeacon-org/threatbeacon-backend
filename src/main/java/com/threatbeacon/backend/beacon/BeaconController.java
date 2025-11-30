package com.threatbeacon.backend.beacon;

import com.threatbeacon.backend.api.dto.MuteRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing direct beacon commands, specifically muting the buzzer.
 */
@RestController
@RequestMapping("/api/beacon")
public class BeaconController {

    private final BeaconStateService beaconStateService;

    public BeaconController(BeaconStateService beaconStateService) {
        this.beaconStateService = beaconStateService;
    }

    /**
     * POST /api/beacon/mute
     * Updates the global buzzerMuted flag in the database.
     * @param request The desired mute state.
     * @return 204 No Content upon success.
     */
    @PostMapping("/mute")
    public ResponseEntity<Void> muteBuzzer(@RequestBody MuteRequestDto request) {
        // Log the action (good practice for security auditing)
        // System.out.println("Buzzer state requested change to: " + request.isMuted());

        beaconStateService.setBuzzerMuted(request.isMuted());

        // Return 204 No Content to indicate successful processing without a body.
        return ResponseEntity.noContent().build();
    }
}
