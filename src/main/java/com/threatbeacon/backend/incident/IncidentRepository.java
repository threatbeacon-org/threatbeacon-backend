package com.threatbeacon.backend.incident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    // Necesitaremos buscar incidentes abiertos para ver si actualizamos uno existente
    // SELECT * FROM incidents WHERE status = 'OPEN' AND type = ?
    Optional<Incident> findFirstByStatusAndType(IncidentStatus status, IncidentType type);

    // Para el dashboard (Overview)
    List<Incident> findByStatusOrderByUpdatedAtDesc(IncidentStatus status);
}