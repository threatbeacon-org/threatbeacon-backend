package com.threatbeacon.backend.beacon;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "beacon_state")
@Data // Lombok: Generates getters, setters, toString, hashCode, equals
@NoArgsConstructor
public class BeaconState {

    // Primary key. We only use ID = 1 for the single global state row.
    @Id
    private Long id = 1L;

    /**
     * Flag to indicate if the buzzer sound is muted (true) or active (false).
     * LED lights should still reflect the risk level regardless of this flag.
     * Note: Mapped to buzzer_muted in the database.
     */
    private boolean buzzerMuted = false;

    /**
     * Timestamp of the last time the state was updated.
     * Mapped to updated_at in the database.
     */
    private ZonedDateTime updatedAt = ZonedDateTime.now();
}
