package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Commentaire")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommentaire;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;

    @Column(nullable = false)
    private String contenu;

    @Column(nullable = false)
    private Double dateCommentaire;

    @Column(nullable = false)
    private Boolean estPrive = false;
}
