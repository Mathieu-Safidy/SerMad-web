package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Demande")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;

    private String libelle;

    private String reference;

    private Double dateDebut;

    private Double dateFin;

    @ManyToOne
    @JoinColumn(name = "id_statutdemande", nullable = false)
    private StatutDemande statutDemande;

    @ManyToOne
    @JoinColumn(name = "id_proceduremere", nullable = false)
    private ProcedureMere procedureMere;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;
}
