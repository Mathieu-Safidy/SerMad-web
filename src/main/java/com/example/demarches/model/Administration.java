package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Administration")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Administration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdministration;

    @Column(name = "libelle_", nullable = false)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "id_typeadm", nullable = false)
    private TypeAdm typeAdm;
}
