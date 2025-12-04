package com.threatbeacon.backend.incident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    // We'll need to search for open incidents to see if we can update an existing one.
    // SELECT * FROM incidents WHERE status = 'OPEN' AND type = ?
    Optional<Incident> findFirstByStatusAndType(IncidentStatus status, IncidentType type);

    // For the dashboard (Overview)
    List<Incident> findByStatusOrderByUpdatedAtDesc(IncidentStatus status);
}