package com.revente.backend.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.revente.backend.infrastructure.persistence.entity.EventEntity;
import com.revente.backend.infrastructure.persistence.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Component
// @Profile("dev") - Enabled for all profiles to ensure seeding matches
// requirements
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    @Override
    public void run(String... args) throws Exception {
        if (eventRepository.count() == 0) {
            seedEvents();
        }
    }

    private void seedEvents() {
        EventEntity event1 = new EventEntity();
        event1.setTitle("Concierto de Grupo 5");
        event1.setDescription("El grupo de oro del Perú en un concierto inolvidable.");
        event1.setLocation("Estadio Nacional, Lima");
        event1.setEventDate(LocalDateTime.now().plusDays(30));
        event1.setCategory(EventEntity.EventCategory.CONCERT);
        event1.setImageUrl(
                "https://firebasestorage.googleapis.com/v0/b/revente-app.appspot.com/o/events%2Fgrupo5.jpg?alt=media");
        event1.setStatus(EventEntity.EventStatus.ACTIVE);

        EventEntity event2 = new EventEntity();
        event2.setTitle("Universitario vs Alianza Lima");
        event2.setDescription("El clásico del fútbol peruano.");
        event2.setLocation("Estadio Monumental, Lima");
        event2.setEventDate(LocalDateTime.now().plusDays(15));
        event2.setCategory(EventEntity.EventCategory.SPORTS);
        event2.setImageUrl(
                "https://firebasestorage.googleapis.com/v0/b/revente-app.appspot.com/o/events%2Fclasico.jpg?alt=media");
        event2.setStatus(EventEntity.EventStatus.ACTIVE);

        EventEntity event3 = new EventEntity();
        event3.setTitle("Hamilton - El Musical");
        event3.setDescription("La obra de teatro más aclamada llega a Lima.");
        event3.setLocation("Teatro Municipal, Lima");
        event3.setEventDate(LocalDateTime.now().plusDays(45));
        event3.setCategory(EventEntity.EventCategory.THEATER);
        event3.setImageUrl(
                "https://firebasestorage.googleapis.com/v0/b/revente-app.appspot.com/o/events%2Fhamilton.jpg?alt=media");
        event3.setStatus(EventEntity.EventStatus.ACTIVE);

        eventRepository.saveAll(List.of(event1, event2, event3));
        System.out.println("DataSeeder: 3 Eventos insertados correctamente.");
    }
}
