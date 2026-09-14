package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ProcedureFille")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProcedureFille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProcedureFille;

    private Double delai;

    private Double cout;
}
