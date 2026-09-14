package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FormulaireFille")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FormulaireFille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormulaireFille;

    @Column(nullable = false)
    private String libelle;
}
