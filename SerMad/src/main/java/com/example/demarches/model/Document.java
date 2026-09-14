package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Document")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocument;

    @Column(name = "libelle_", nullable = false)
    private String libelle;

    private Integer ageMinimum;

    private Double dateExpiration;

    private Double dateDelivrance;

    @ManyToOne
    @JoinColumn(name = "id_categorie", nullable = false)
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "id_administration", nullable = false)
    private Administration administration;

    @ManyToOne
    @JoinColumn(name = "id_typedocument", nullable = false)
    private TypeDocument typeDocument;
}
