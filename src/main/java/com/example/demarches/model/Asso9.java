package com.example.demarches.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Asso_9")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asso9 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user_", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_document", nullable = false)
    private Document document;

    private Double dateObtention;
}
