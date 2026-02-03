package com.revente.backend.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String username;

    @Column(unique = true, length = 8) // Nullable now
    private String dni;

    private String bio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(unique = true)
    private String phone;

    @Column(name = "firebase_uid", unique = true)
    private String firebaseUid;

    @Column(name = "avg_rating")
    private BigDecimal avgRating;

    @Column(name = "is_verified")
    private boolean isVerified;
}
