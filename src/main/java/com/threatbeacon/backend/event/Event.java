package com.threatbeacon.backend.event;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_severity", columnList = "severity"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_type_timestamp", columnList = "type, timestamp")  // Composite index para queries comunes
})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(length = 45)
    private String ip;

    @Column(length = 5)
    private String country;

    @Column(nullable = false, length = 20)
    private String severity;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    // Constructors
    public Event() {
    }

    public Event(String type, String source, String ip, String country,
                 String severity, OffsetDateTime timestamp, String metadata) {
        this.type = type;
        this.source = source;
        this.ip = ip;
        this.country = country;
        this.severity = severity;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    // ✅ CORREGIDO: Se eliminó el parámetro innecesario
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}