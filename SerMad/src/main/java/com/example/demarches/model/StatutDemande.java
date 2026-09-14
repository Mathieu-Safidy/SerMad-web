package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "StatutDemande")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatutDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatutDemande;

    @Column(nullable = false)
    private String libelle;
}
