package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "User_")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private String motDePasse;

    @Column(unique = true)
    private String cin;

    private String dateNaissance;

    private String lieuNaissance;

    private String nationalite;

    private String nomPere;

    private String prenomPere;

    private String nomMere;

    private String prenomMere;

    private String adresseResidence;

    @ManyToOne
    @JoinColumn(name = "id_fokontany_residence")
    private Fokontany fokontanyResidence;

    @Column(nullable = false)
    private Boolean estActif = true;
}
