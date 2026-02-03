package com.revente.backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDTO {

    @NotBlank(message = "El UID de Firebase es obligatorio")
    private String firebaseUid;

    @NotBlank(message = "El número de celular es obligatorio")
    private String phone;

    // DNI is now optional for initial registration
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
    private String dni;

    private String fullName;
    private String email;
}
