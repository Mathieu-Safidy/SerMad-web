package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Validation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idValidation;

    private String libelle;

    private Double dateValidation;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;
}
