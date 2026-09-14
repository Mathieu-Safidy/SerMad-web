package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TokenQRCode")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TokenQRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTokenQR;

    @Column(unique = true, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;

    private Double dateExpiration;

    @Column(nullable = false)
    private Boolean estUtilise = false;
}
