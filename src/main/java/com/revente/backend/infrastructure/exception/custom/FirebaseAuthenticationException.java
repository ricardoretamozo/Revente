package com.revente.backend.infrastructure.exception.custom;

public class FirebaseAuthenticationException extends RuntimeException {
    public FirebaseAuthenticationException(String message) {
        super(message);
    }
}
