package com.threatbeacon.backend.beacon;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing and managing the single BeaconState entity.
 */
public interface BeaconStateRepository extends JpaRepository<BeaconState, Long> {
}
