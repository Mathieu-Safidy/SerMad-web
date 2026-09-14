package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TypeDocument")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeDocument;

    @Column(nullable = false)
    private String libelle;
}
