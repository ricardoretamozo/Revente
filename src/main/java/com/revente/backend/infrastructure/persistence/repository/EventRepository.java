package com.revente.backend.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revente.backend.infrastructure.persistence.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    // Find all events that are active and ordered by date
    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM events WHERE status = :status ORDER BY event_date ASC", nativeQuery = true)
    List<EventEntity> findActiveEvents(@org.springframework.data.repository.query.Param("status") String status);
}
