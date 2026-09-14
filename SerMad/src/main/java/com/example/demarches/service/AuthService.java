package com.example.demarches.service;

import com.example.demarches.dto.*;
import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import com.example.demarches.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final Asso3Repository asso3Repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String profil = getUserProfile(user.getIdUser());

        return new AuthResponse(
                token,
                user.getIdUser(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                profil
        );
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .cin(request.getCin())
                .dateNaissance(request.getDateNaissance())
                .estActif(true)
                .build();

        user = userRepository.save(user);

        Profile citoyenProfile = profileRepository.findByNom("Citoyen")
                .orElseThrow(() -> new RuntimeException("Profil Citoyen non trouvé"));

        Asso3 asso3 = Asso3.builder()
                .user(user)
                .profile(citoyenProfile)
                .build();
        asso3Repository.save(asso3);

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getIdUser(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                "Citoyen"
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    private String getUserProfile(Long userId) {
        List<Asso3> profils = asso3Repository.findByUserIdUser(userId);
        if (profils.isEmpty()) return "Aucun";
        return profils.get(0).getProfile().getNom();
    }
}
