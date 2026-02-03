package com.revente.backend.application.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProfileRequestDTO {

    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    private String username;

    @Size(max = 500, message = "La biografía no puede exceder los 500 caracteres")
    private String bio;

    private String profileImageUrl;
}
