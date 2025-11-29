package com.threatbeacon.backend.risk;

/**
 * Defines the three possible global risk levels for the SOC, based on active incidents.
 * This enum is used by the RiskService, RiskController, Frontend, and the IoT Beacon.
 */
public enum RiskLevel {
    // Normal activity. No active critical/high incidents.
    NORMAL,

    // Suspicious activity detected (medium severity incidents).
    SUSPICIOUS,

    // Critical attack detected (high/critical severity incidents). Immediate action required.
    CRITICAL
}
