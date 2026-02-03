package com.revente.backend.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.revente.backend.common.ApiError;
import com.revente.backend.infrastructure.exception.custom.EntityNotFoundException;
import com.revente.backend.infrastructure.exception.custom.FirebaseAuthenticationException;
import com.revente.backend.infrastructure.exception.custom.InsufficientStockException;
import com.revente.backend.infrastructure.exception.custom.UserAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                "VALIDACION_FALLIDA",
                "Error de validación: Verifique los datos enviados",
                details,
                LocalDateTime.now());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(
                "BAD_REQUEST",
                ex.getMessage(), // Mensaje directo de la excepción (ya traducido)
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ApiError apiError = new ApiError(
                "USER_EXISTS",
                "El número de celular o DNI ya se encuentra registrado",
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(FirebaseAuthenticationException.class)
    public ResponseEntity<ApiError> handleFirebaseAuthentication(FirebaseAuthenticationException ex) {
        ApiError apiError = new ApiError(
                "AUTH_ERROR",
                "La sesión ha expirado o el código es inválido",
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(
                "NOT_FOUND",
                "El recurso solicitado no existe",
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError> handleInsufficientStock(InsufficientStockException ex) {
        ApiError apiError = new ApiError(
                "NO_STOCK",
                "Lo sentimos, las entradas ya no están disponibles",
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        ApiError apiError = new ApiError(
                "INTERNAL_SERVER_ERROR",
                "Ha ocurrido un error inesperado",
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleJsonError(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError(
                "INVALID_JSON",
                "El formato de la solicitud es inválido (JSON incorrecto)",
                List.of(ex.getMessage()),
                LocalDateTime.now());
        return ResponseEntity.badRequest().body(apiError);
    }
}
