package com.revente.backend.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthCheckResponseDTO {
    private boolean exists;
    private boolean isProfileComplete; // Has username/bio
    private boolean isKyVerified; // Has DNI/Bank
}
