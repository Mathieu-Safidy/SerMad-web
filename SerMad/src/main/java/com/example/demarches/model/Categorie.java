package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categorie")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorie;

    @Column(name = "libellle", nullable = false)
    private String libelle;
}
