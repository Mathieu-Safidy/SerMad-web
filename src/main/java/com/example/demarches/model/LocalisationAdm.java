package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LocalisationAdm")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LocalisationAdm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocalisationAdm;

    private String libelle;

    private String adresse;

    private Double longitude;

    private Double latitude;

    private String codePostal;

    @ManyToOne
    @JoinColumn(name = "id_administration", nullable = false)
    private Administration administration;
}
