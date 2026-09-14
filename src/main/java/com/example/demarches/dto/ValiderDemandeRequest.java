package com.example.demarches.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValiderDemandeRequest {
    @NotNull
    private Long idDemande;
}
