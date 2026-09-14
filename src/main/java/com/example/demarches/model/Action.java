package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Action_")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAction;

    @Column(name = "libelle_", nullable = false)
    private String libelle;
}
