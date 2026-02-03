package com.revente.backend.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revente.backend.infrastructure.persistence.entity.EventEntity;
import com.revente.backend.infrastructure.persistence.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<EventEntity> getActiveEvents() {
        return eventRepository.findActiveEvents(EventEntity.EventStatus.ACTIVE.name());
    }
}
