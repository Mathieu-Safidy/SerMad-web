package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Entite")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Entite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEntite;

    @Column(nullable = false)
    private String libelle;
}
