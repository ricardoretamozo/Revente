package com.revente.backend.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revente.backend.application.dto.AuthCheckResponseDTO;
import com.revente.backend.application.dto.AuthResponseDTO;
import com.revente.backend.application.dto.LoginRequestDTO;
import com.revente.backend.application.service.AuthService;
import com.revente.backend.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/check-status")
    public ResponseEntity<ApiResponse<AuthCheckResponseDTO>> checkStatus(
            @RequestBody java.util.Map<String, String> request) {
        String phone = request.get("phone");
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("El número de celular es obligatorio");
        }
        return ResponseEntity.ok(ApiResponse.success(authService.checkStatus(phone), "Estado del usuario verificado"));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @Valid @RequestBody com.revente.backend.application.dto.SendOtpRequestDTO request) {
        authService.sendOtp(request.getPhone());
        return ResponseEntity.ok(ApiResponse.success(null, "Código OTP enviado exitosamente"));
    }

    @PostMapping("/login-with-phone")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> loginWithPhone(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.loginWithPhone(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login exitoso"));
    }
}
