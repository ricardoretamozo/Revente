package com.revente.backend.application.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revente.backend.application.dto.AuthCheckResponseDTO;
import com.revente.backend.infrastructure.persistence.entity.UserEntity;
import com.revente.backend.infrastructure.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void checkStatus_WhenUserExists_ShouldReturnExistsTrue() {
        // Arrange
        String phone = "+51999888777";
        UserEntity mockUser = new UserEntity();
        mockUser.setPhone(phone);
        mockUser.setUsername("ricardo"); // Profile complete

        Mockito.when(userRepository.findByPhone(phone)).thenReturn(Optional.of(mockUser));

        // Act
        AuthCheckResponseDTO response = authService.checkStatus(phone);

        // Assert
        Assertions.assertTrue(response.isExists());
        Assertions.assertTrue(response.isProfileComplete());
        Assertions.assertFalse(response.isKyVerified());
    }

    @Test
    void checkStatus_WhenUserDoesNotExist_ShouldReturnExistsFalse() {
        // Arrange
        String phone = "+51000000000";
        Mockito.when(userRepository.findByPhone(phone)).thenReturn(Optional.empty());

        // Act
        AuthCheckResponseDTO response = authService.checkStatus(phone);

        // Assert
        Assertions.assertFalse(response.isExists());
    }
}
