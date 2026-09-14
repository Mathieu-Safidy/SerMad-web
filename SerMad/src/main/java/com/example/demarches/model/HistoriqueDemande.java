package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HistoriqueDemande")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HistoriqueDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorique;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Double dateAction;

    private Long ancienStatut;

    private Long nouveauStatut;
}
