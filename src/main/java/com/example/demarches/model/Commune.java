package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Commune")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommune;

    @Column(name = "libelle_", nullable = false)
    private String libelle;
}