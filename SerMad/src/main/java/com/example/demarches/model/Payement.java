package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Payement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayement;

    private String libelle;

    private Double datePayement;

    @ManyToOne
    @JoinColumn(name = "id_procedurefille", nullable = false)
    private ProcedureFille procedureFille;
}
