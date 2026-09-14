package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Service_")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idService;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "id_administration", nullable = false)
    private Administration administration;
}
