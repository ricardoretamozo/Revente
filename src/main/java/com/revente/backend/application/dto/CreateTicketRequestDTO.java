package com.revente.backend.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTicketRequestDTO {

    @NotNull(message = "El ID del evento es obligatorio")
    private UUID eventId;

    @NotBlank(message = "La sección es obligatoria")
    private String section;

    @NotBlank(message = "Fila/Asiento es obligatorio")
    private String rowSeat;

    @NotNull(message = "El precio original es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio original debe ser mayor a 0")
    private BigDecimal originalPrice;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    private BigDecimal listingPrice;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;
}
