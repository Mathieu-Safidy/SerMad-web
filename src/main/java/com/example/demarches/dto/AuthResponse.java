package com.example.demarches.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private String profil;
}
