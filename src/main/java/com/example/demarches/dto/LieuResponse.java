package com.example.demarches.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LieuResponse {
    private Long idAdministration;
    private String libelle;
    private String adresse;
    private Double longitude;
    private Double latitude;
    private String codePostal;
    private String raison;
}