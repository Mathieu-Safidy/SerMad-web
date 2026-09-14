package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TypeAdm")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeAdm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeAdm;

    @Column(nullable = false)
    private String libelle;
}
