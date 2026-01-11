package com.org.biblio.notification;

import com.org.emprunt.event.EmpruntCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(topics = "emprunt-created", groupId = "${spring.kafka.consumer.group-id}")
    public void onEmpruntCreated(EmpruntCreatedEvent event) {
        System.out.println("[NOTIFICATION] EMPRUNT_CREATED reçu: " + event);
        System.out.println("[NOTIFICATION] => User " + event.getUserId()
                + " a emprunté Book " + event.getBookId()
                + " (empruntId=" + event.getEmpruntId() + ")");
    }
}