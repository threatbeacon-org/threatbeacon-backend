package com.threatbeacon.backend.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // ===== QUERIES FOR RECENT EVENTS LOOKUPS =====

    /**
     * Find recent events ordered by timestamp descending (most recent first)
     * Useful for dashboard/monitoring views
     */
    List<Event> findTop100ByOrderByTimestampDesc();

    /**
     * Find events within a time range, ordered by timestamp
     * Optimized with timestamp index
     */
    List<Event> findByTimestampBetweenOrderByTimestampDesc(
            OffsetDateTime startTime,
            OffsetDateTime endTime
    );

    /**
     * Paginated recent events for better performance on large datasets
     */
    Page<Event> findAllByOrderByTimestampDesc(Pageable pageable);

    // ===== QUERIES BY TYPE (for incident detection) =====

    /**
     * Find recent events of a specific type
     * Optimized with composite index (type, timestamp)
     */
    List<Event> findByTypeOrderByTimestampDesc(String type);

    /**
     * Find events of a specific type within a time range
     * Useful for analyzing attack patterns
     */
    List<Event> findByTypeAndTimestampBetweenOrderByTimestampDesc(
            String type,
            OffsetDateTime startTime,
            OffsetDateTime endTime
    );

    /**
     * Count events of a specific type within a time range
     * Efficient for spike detection logic
     */
    Long countByTypeAndTimestampBetween(
            String type,
            OffsetDateTime startTime,
            OffsetDateTime endTime
    );

    // ===== QUERIES BY SEVERITY =====

    /**
     * Find recent events by severity level
     * Optimized with severity index
     */
    List<Event> findBySeverityOrderByTimestampDesc(String severity);

    /**
     * Find high-severity events within a time range
     */
    List<Event> findBySeverityAndTimestampBetweenOrderByTimestampDesc(
            String severity,
            OffsetDateTime startTime,
            OffsetDateTime endTime
    );

    // ===== QUERIES BY IP (for tracking specific attackers) =====

    /**
     * Find all events from a specific IP address
     * Useful for investigating potential attackers
     */
    List<Event> findByIpOrderByTimestampDesc(String ip);

    /**
     * Count events from a specific IP within a time range
     * Useful for rate limiting / brute force detection
     */
    Long countByIpAndTimestampBetween(
            String ip,
            OffsetDateTime startTime,
            OffsetDateTime endTime
    );

    // ===== COMPLEX QUERIES WITH @Query =====

    /**
     * Get event count grouped by type within a time range
     * Useful for analytics and dashboard metrics
     */
    @Query("SELECT e.type, COUNT(e) FROM Event e " +
            "WHERE e.timestamp BETWEEN :startTime AND :endTime " +
            "GROUP BY e.type " +
            "ORDER BY COUNT(e) DESC")
    List<Object[]> countEventsByTypeInTimeRange(
            @Param("startTime") OffsetDateTime startTime,
            @Param("endTime") OffsetDateTime endTime
    );

    /**
     * Get most active IPs within a time range
     * Useful for identifying potential attackers
     */
    @Query("SELECT e.ip, COUNT(e) FROM Event e " +
            "WHERE e.timestamp BETWEEN :startTime AND :endTime " +
            "AND e.ip IS NOT NULL " +
            "GROUP BY e.ip " +
            "ORDER BY COUNT(e) DESC")
    List<Object[]> findMostActiveIpsInTimeRange(
            @Param("startTime") OffsetDateTime startTime,
            @Param("endTime") OffsetDateTime endTime,
            Pageable pageable
    );
}