package com.revente.backend.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revente.backend.application.dto.AuthResponseDTO;
import com.revente.backend.application.dto.UpdateProfileRequestDTO;
import com.revente.backend.application.service.UserService;
import com.revente.backend.common.ApiResponse;
import com.revente.backend.infrastructure.persistence.entity.UserEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<AuthResponseDTO.UserSummary>> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDTO request) {

        // Extract User ID from Security Context (set by JwtAuthenticationFilter)
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity updatedUser = userService.updateProfile(userId, request);

        return ResponseEntity.ok(ApiResponse.success(
                AuthResponseDTO.UserSummary.fromEntity(updatedUser),
                "Perfil actualizado correctamente"));
    }
}
