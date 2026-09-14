package com.example.demarches.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DemandeRequest {
    private String libelle;

    @NotBlank
    private String reference;

    private Long idProcedureMere;

    private Long idCitoyen;
}
