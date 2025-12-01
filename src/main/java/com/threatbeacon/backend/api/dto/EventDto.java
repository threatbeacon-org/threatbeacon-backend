package com.threatbeacon.backend.api.dto;

import java.time.OffsetDateTime;

public class EventDto {

    private String type;
    private String source;
    private String ip;
    private String country;
    private String severity;
    private OffsetDateTime timestamp;
    private String metadata;

    // Constructors
    public EventDto() {
    }

    public EventDto(String type, String source, String ip, String country,
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