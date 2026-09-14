package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Asso_2", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_profile", "id_action", "id_entite"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asso2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_profile", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "id_action", nullable = false)
    private Action action;

    @ManyToOne
    @JoinColumn(name = "id_entite", nullable = false)
    private Entite entite;
}
