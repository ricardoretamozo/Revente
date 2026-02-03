package com.revente.backend.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.revente.backend.application.dto.CreateTicketRequestDTO;
import com.revente.backend.infrastructure.exception.custom.EntityNotFoundException;
import com.revente.backend.infrastructure.persistence.entity.EventEntity;
import com.revente.backend.infrastructure.persistence.entity.TicketEntity;
import com.revente.backend.infrastructure.persistence.entity.UserEntity;
import com.revente.backend.infrastructure.persistence.repository.EventRepository;
import com.revente.backend.infrastructure.persistence.repository.TicketRepository;
import com.revente.backend.infrastructure.persistence.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

        private final TicketRepository ticketRepository;
        private final EventRepository eventRepository;
        private final UserRepository userRepository;
        private final FileStorageService fileStorageService;

        @Transactional
        public TicketEntity createListing(String userId, CreateTicketRequestDTO request,
                        org.springframework.web.multipart.MultipartFile file) {
                // 1. Validate User
                UserEntity seller = userRepository.findById(UUID.fromString(userId))
                                .orElseThrow(() -> new EntityNotFoundException("Usuario vendedor no encontrado"));

                // 2. Validate Event
                EventEntity event = eventRepository.findById(request.getEventId())
                                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

                // 3. Store File
                String filePath = null;
                if (file != null && !file.isEmpty()) {
                        filePath = fileStorageService.store(file, "tickets");
                }

                // 4. Create Ticket
                TicketEntity ticket = new TicketEntity();
                ticket.setSeller(seller);
                ticket.setEvent(event);
                ticket.setSection(request.getSection());
                ticket.setRowSeat(request.getRowSeat());
                ticket.setOriginalPrice(request.getOriginalPrice());
                ticket.setListingPrice(request.getListingPrice());
                ticket.setQuantity(request.getQuantity());
                ticket.setStatus(TicketEntity.TicketStatus.AVAILABLE);
                ticket.setSecureStoragePath(filePath);

                return ticketRepository.save(ticket);
        }
}
