package com.revente.backend.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.revente.backend.application.dto.UpdateProfileRequestDTO;
import com.revente.backend.infrastructure.exception.custom.EntityNotFoundException;
import com.revente.backend.infrastructure.persistence.entity.UserEntity;
import com.revente.backend.infrastructure.persistence.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity updateProfile(String userId, UpdateProfileRequestDTO request) {
        UserEntity user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Update Username
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            // Check uniqueness if changed
            if (!request.getUsername().equals(user.getUsername())
                    && userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso");
            }
            user.setUsername(request.getUsername());
        }

        // Update Bio
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        // Update Profile Image
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }

        return userRepository.save(user);
    }
}
