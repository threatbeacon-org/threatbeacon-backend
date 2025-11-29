package com.threatbeacon.backend.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Request body for POST /api/beacon/mute to set the buzzer state.
 */
@Getter
@Setter
public class MuteRequestDto {
    private boolean muted; // The desired state: true (mute) or false (unmute)
}
