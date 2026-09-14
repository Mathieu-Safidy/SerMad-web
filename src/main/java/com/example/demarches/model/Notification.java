package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Notification")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotification;

    private String objetNotif;

    private String corpsNotif;

    private Double dateNotif;

    @Column(nullable = false)
    private Boolean estLue = false;
}
