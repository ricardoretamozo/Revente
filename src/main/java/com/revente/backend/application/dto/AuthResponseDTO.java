package com.revente.backend.application.dto;

import com.revente.backend.infrastructure.persistence.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private UserSummary user;

    @Data
    @Builder
    @AllArgsConstructor
    public static class UserSummary {
        private String id;
        private String phone;
        private String fullName;
        private boolean isVerified;
        private boolean isProfileComplete;

        public static UserSummary fromEntity(UserEntity entity) {
            // Profile is considered complete if they have set a username
            // (Assuming auto-generated usernames might need a different check,
            // but effectively if they went through onboarding, they should have this set)
            boolean profileComplete = entity.getUsername() != null && !entity.getUsername().isBlank();

            return UserSummary.builder()
                    .id(entity.getId() != null ? entity.getId().toString() : null)
                    .phone(entity.getPhone())
                    .fullName(entity.getFullName())
                    .isVerified(entity.isVerified()) // This is KYC verification (DNI)
                    .isProfileComplete(profileComplete)
                    .build();
        }
    }
}
