package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Arrondissement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Arrondissement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArrondissement;

    @Column(name = "libelle_", nullable = false)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "id_commune", nullable = false)
    private Commune commune;
}