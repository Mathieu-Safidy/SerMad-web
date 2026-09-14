package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Asso_12")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asso12 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_document", nullable = false)
    private Document document;

    @ManyToOne
    @JoinColumn(name = "id_procedurefille", nullable = false)
    private ProcedureFille procedureFille;
}
