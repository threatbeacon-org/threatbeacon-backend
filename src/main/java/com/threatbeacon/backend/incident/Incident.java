package com.threatbeacon.backend.incident;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentSeverity severity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "event_count")
    private Integer eventCount;

    // Esto por el momento lo vamos aguardar en "CVS"(Comma Separated Values) en texto
    @Column(name = "main_ips", columnDefinition = "TEXT")
    private String mainIps;

    @Column(name = "countries", columnDefinition = "TEXT")
    private String countries;

    //Hook de JPA para asignar fechas automáticamnete antes de insertar
    @PrePersist
    protected void onCreate(){
        if (createdAt == null) createdAt = Instant.now();
        if (updatedAt == null) updatedAt = Instant.now();
        if (eventCount == null) eventCount = 1;
        if (status == null) status = IncidentStatus.OPEN;
    }

    //Hook para actualizar fecha al editar
    @PreUpdate
    protected void onUpdate(){
        updatedAt = Instant.now();
    }
}