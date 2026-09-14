package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Asso_3")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asso3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_profile", nullable = false)
    private Profile profile;
}
