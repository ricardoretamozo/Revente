package com.revente.backend.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revente.backend.application.dto.AuthCheckResponseDTO;
import com.revente.backend.application.dto.AuthResponseDTO;
import com.revente.backend.application.dto.LoginRequestDTO;
import com.revente.backend.infrastructure.exception.custom.FirebaseAuthenticationException;
import com.revente.backend.infrastructure.persistence.entity.UserEntity;
import com.revente.backend.infrastructure.persistence.repository.UserRepository;
import com.revente.backend.infrastructure.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final com.revente.backend.infrastructure.external.GoogleIdentityService googleIdentityService;
    private final OtpSessionManager otpSessionManager;

    public AuthCheckResponseDTO checkStatus(String phone) {
        Optional<UserEntity> userOpt = userRepository.findByPhone(phone);

        if (userOpt.isEmpty()) {
            return AuthCheckResponseDTO.builder()
                    .exists(false)
                    .isProfileComplete(false)
                    .isKyVerified(false)
                    .build();
        }

        UserEntity user = userOpt.get();
        return AuthCheckResponseDTO.builder()
                .exists(true)
                .isProfileComplete(user.getUsername() != null)
                .isKyVerified(user.getDni() != null)
                .build();
    }

    public void sendOtp(String phone) {
        String sessionInfo = googleIdentityService.sendOtp(phone);
        otpSessionManager.saveSession(phone, sessionInfo);
    }

    @Transactional
    public AuthResponseDTO loginWithPhone(LoginRequestDTO request) {
        // 1. Verify OTP with Firebase (Real Exchange via Google Identity Toolkit)
        String firebaseIdToken = verifyOtpAndGetIdToken(request.getPhone(), request.getOtpCode());

        // Extract UID from ID Token (Verify signature too for security, or trust Google
        // response)
        // Here we verify it again using Admin SDK to be sure and get the UID clean
        String firebaseUid = getUidFromIdToken(firebaseIdToken);

        // 2. Find or Create User
        Optional<UserEntity> existingUser = userRepository.findByPhone(request.getPhone());

        UserEntity user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // Auto Register
            UserEntity newUser = new UserEntity();
            newUser.setPhone(request.getPhone());
            newUser.setFirebaseUid(firebaseUid);
            newUser.setVerified(true);
            newUser.setFullName("User " + request.getPhone().substring(Math.max(0, request.getPhone().length() - 4)));
            user = userRepository.save(newUser);
        }

        // 3. Generate JWT
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .user(AuthResponseDTO.UserSummary.fromEntity(user))
                .build();
    }

    private String verifyOtpAndGetIdToken(String phone, String otpCode) {
        String sessionInfo = otpSessionManager.getSession(phone);
        if (sessionInfo == null) {
            throw new FirebaseAuthenticationException(
                    "No hay sesión de OTP activa para este número. Solicite un nuevo código.");
        }

        String idToken = googleIdentityService.verifyOtp(sessionInfo, otpCode);
        otpSessionManager.removeSession(phone); // Clear session after use
        return idToken;
    }

    private String getUidFromIdToken(String idToken) {
        // Handle Mock Token
        if (idToken != null && idToken.startsWith("MOCK_ID_TOKEN:")) {
            String phone = idToken.split(":")[1];
            // Deterministic UID for mock users
            return "firebase_uid_" + phone.replace("+", "");
        }

        try {
            // Use Admin SDK to verify the token returned by REST API and extract UID
            com.google.firebase.auth.FirebaseToken decoded = com.google.firebase.auth.FirebaseAuth.getInstance()
                    .verifyIdToken(idToken);
            return decoded.getUid();
        } catch (com.google.firebase.auth.FirebaseAuthException e) {
            throw new FirebaseAuthenticationException("Error validando el token de identidad: " + e.getMessage());
        }
    }
}
