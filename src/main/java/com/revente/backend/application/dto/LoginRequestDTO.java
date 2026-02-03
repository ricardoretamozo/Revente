package com.revente.backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El número de celular es obligatorio")
    private String phone;

    @NotBlank(message = "El código OTP es obligatorio")
    private String otpCode;
}
