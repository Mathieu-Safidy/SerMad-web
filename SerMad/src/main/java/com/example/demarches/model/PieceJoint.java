package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PieceJoint")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PieceJoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPieceJoint;

    private String lien;

    private String type;

    @ManyToOne
    @JoinColumn(name = "id_notification", nullable = false)
    private Notification notification;
}
