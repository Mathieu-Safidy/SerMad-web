package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Profile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfile;

    @Column(nullable = false)
    private String nom;
}
