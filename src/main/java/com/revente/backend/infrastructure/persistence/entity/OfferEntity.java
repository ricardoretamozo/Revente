package com.revente.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", nullable = false)
    private UserEntity bidder;

    @Column(name = "amount_offered", nullable = false)
    private BigDecimal amountOffered;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum OfferStatus {
        PENDING, ACCEPTED, REJECTED, EXPIRED
    }
}
