package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Asso_23")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asso23 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_notification", nullable = false)
    private Notification notification;
}
