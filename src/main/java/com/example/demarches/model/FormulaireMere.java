package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FormulaireMere")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FormulaireMere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormulaire;

    private String libelle;

    @ManyToOne
    @JoinColumn(name = "id_procedurefille")
    private ProcedureFille procedureFille;

    @ManyToOne
    @JoinColumn(name = "id_formulairefille", nullable = false)
    private FormulaireFille formulaireFille;
}
