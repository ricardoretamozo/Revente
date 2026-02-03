package com.revente.backend.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revente.backend.application.service.EventService;
import com.revente.backend.common.ApiResponse;
import com.revente.backend.infrastructure.persistence.entity.EventEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventEntity>>> getEvents() {
        return ResponseEntity.ok(ApiResponse.success(eventService.getActiveEvents(), "Eventos activos"));
    }
}
