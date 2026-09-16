package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Fokontany")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Fokontany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFokontany;

    @Column(name = "libelle_", nullable = false)
    private String libelle;

    @Column(name = "commune_rurale_")
    private String communeRurale;

    @ManyToOne
    @JoinColumn(name = "id_arrondissement", nullable = false)
    private Arrondissement arrondissement;
}