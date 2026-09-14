package com.example.demarches.controller;

import com.example.demarches.model.*;
import com.example.demarches.service.AuthService;
import com.example.demarches.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = authService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.getNotificationsByUser(user.getIdUser()));
    }

    @PutMapping("/{id}/lire")
    public ResponseEntity<Notification> marquerLue(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.marquerLue(id));
    }
}
