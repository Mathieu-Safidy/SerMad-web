package com.example.demarches.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @Email
    @NotBlank
    private String email;

    private String telephone;

    @NotBlank
    private String motDePasse;

    private String cin;

    @NotBlank
    private String dateNaissance;
}
