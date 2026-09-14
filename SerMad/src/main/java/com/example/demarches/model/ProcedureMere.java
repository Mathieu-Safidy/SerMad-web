package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ProcedureMere")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProcedureMere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProcedureMere;

    @ManyToOne
    @JoinColumn(name = "id_document", nullable = false)
    private Document document;

    @ManyToOne
    @JoinColumn(name = "id_procedurefille", nullable = false)
    private ProcedureFille procedureFille;
}
