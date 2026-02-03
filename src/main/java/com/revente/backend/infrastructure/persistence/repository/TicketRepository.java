package com.revente.backend.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revente.backend.infrastructure.persistence.entity.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    // Find valid tickets for an event (Active listings only)
    // We will use native query casting trick here too just in case, or try JPA
    // first if we trust the previous fix.
    // However, for consistency and robustness, let's stick to simple JPA derived
    // queries first,
    // assuming the URL fix handles it. If not, we fall back to native.

    // Actually, let's use the safer native query approach from the start to avoid
    // "operator does not exist" errors again.

    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM tickets WHERE event_id = :eventId AND status = CAST(:status AS ticket_status)", nativeQuery = true)
    List<TicketEntity> findByEventIdAndStatus(UUID eventId, String status);
}
