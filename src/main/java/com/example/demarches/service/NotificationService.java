package com.example.demarches.service;

import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final Asso23Repository asso23Repository;
    private final UserRepository userRepository;

    public Notification creerNotification(Long userId, String objet, String corps) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Notification notification = Notification.builder()
                .objetNotif(objet)
                .corpsNotif(corps)
                .dateNotif((double) Instant.now().toEpochMilli())
                .estLue(false)
                .build();

        notification = notificationRepository.save(notification);

        Asso23 asso23 = Asso23.builder()
                .user(user)
                .notification(notification)
                .build();
        asso23Repository.save(asso23);

        return notification;
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        List<Asso23> asso23List = asso23Repository.findByUserIdUser(userId);
        return asso23List.stream()
                .map(Asso23::getNotification)
                .toList();
    }

    public Notification marquerLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setEstLue(true);
        return notificationRepository.save(notification);
    }
}
