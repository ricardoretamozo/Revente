package com.revente.backend.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revente.backend.application.dto.CreateTicketRequestDTO;
import com.revente.backend.application.service.TicketService;
import com.revente.backend.common.ApiResponse;
import com.revente.backend.infrastructure.persistence.entity.TicketEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TicketEntity>> createListing(
            @org.springframework.web.bind.annotation.RequestPart("data") @Valid CreateTicketRequestDTO request,
            @org.springframework.web.bind.annotation.RequestPart("file") org.springframework.web.multipart.MultipartFile file) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TicketEntity createdTicket = ticketService.createListing(userId, request, file);

        return ResponseEntity.ok(ApiResponse.success(createdTicket, "Ticket publicado correctamente"));
    }

    @PostMapping(consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TicketEntity>> createListingJson(
            @org.springframework.web.bind.annotation.RequestBody @Valid CreateTicketRequestDTO request) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Call service with null file
        TicketEntity createdTicket = ticketService.createListing(userId, request, null);

        return ResponseEntity.ok(ApiResponse.success(createdTicket, "Ticket publicado correctamente (sin archivo)"));
    }
}
