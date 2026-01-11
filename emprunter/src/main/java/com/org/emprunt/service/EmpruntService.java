package com.org.emprunt.service;

import com.org.emprunt.DTO.EmpruntDetailsDTO;
import com.org.emprunt.entities.Emprunter;
import com.org.emprunt.event.EmpruntCreatedEvent;
import com.org.emprunt.feign.BookClient;
import com.org.emprunt.feign.UserClient;
import com.org.emprunt.repositories.EmpruntRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmpruntService {

    private static final String TOPIC_EMPRUNT_CREATED = "emprunt-created";

    private final EmpruntRepository repo;
    private final UserClient userClient;
    private final BookClient bookClient;
    private final KafkaTemplate<String, EmpruntCreatedEvent> kafkaTemplate;

    public EmpruntService(
            EmpruntRepository repo,
            UserClient userClient,
            BookClient bookClient,
            KafkaTemplate<String, EmpruntCreatedEvent> kafkaTemplate
    ) {
        this.repo = repo;
        this.userClient = userClient;
        this.bookClient = bookClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Emprunter createEmprunt(Long userId, Long bookId) {

        // 1. Vérifier user existe
        userClient.getUser(userId);

        // 2. Vérifier book existe
        bookClient.getBook(bookId);

        // 3. Créer l’emprunt (MySQL)
        Emprunter b = new Emprunter();
        b.setUserId(userId);
        b.setBookId(bookId);

        Emprunter saved = repo.save(b);

        // 4. Publier l'événement Kafka (asynchrone)
        EmpruntCreatedEvent event = new EmpruntCreatedEvent(
                saved.getId(),
                userId,
                bookId,
                "EMPRUNT_CREATED",
                LocalDateTime.now()
        );
        kafkaTemplate.send(TOPIC_EMPRUNT_CREATED, event);

        return saved;
    }

    public List<EmpruntDetailsDTO> getAllEmprunts() {
        return repo.findAll().stream().map(e -> {

            var user = userClient.getUser(e.getUserId());
            var book = bookClient.getBook(e.getBookId());

            return new EmpruntDetailsDTO(
                    e.getId(),
                    user.getName(),
                    book.getTitle(),
                    e.getEmpruntDate());
        }).collect(Collectors.toList());
    }
}