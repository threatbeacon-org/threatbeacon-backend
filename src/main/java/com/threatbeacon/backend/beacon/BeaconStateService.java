package com.threatbeacon.backend.beacon;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Service to manage the state of the physical beacon, primarily the buzzerMuted flag.
 * All state modifications must be transactional.
 */
@Service
public class BeaconStateService {

    private final BeaconStateRepository beaconStateRepository;

    public BeaconStateService(BeaconStateRepository beaconStateRepository) {
        this.beaconStateRepository = beaconStateRepository;
    }

    /**
     * Retrieve the current BeaconState. If it doesn't exist, create a new one with default values.
     *
     * @return The current BeaconState.
     */
    public BeaconState getBeaconState() {
        return beaconStateRepository.findById(1L).orElseGet(() -> {
            BeaconState newState = new BeaconState();
            return beaconStateRepository.save(newState);
        });
    }

    /**
     * Update the buzzerMuted flag in the BeaconState.
     *
     * @param muted True to mute the buzzer, false to unmute.
     * @return The updated BeaconState.
     */
    public BeaconState setBuzzerMuted(boolean muted) {
        BeaconState state = getBeaconState();
        state.setBuzzerMuted(muted);
        state.setUpdatedAt(ZonedDateTime.now());
        return beaconStateRepository.save(state);
    }
}
