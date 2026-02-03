package com.revente.backend.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revente.backend.infrastructure.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findByDni(String dni);

    boolean existsByPhone(String phone);

    boolean existsByDni(String dni);

    boolean existsByUsername(String username);
}
