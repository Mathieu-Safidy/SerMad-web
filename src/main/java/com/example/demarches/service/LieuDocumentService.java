package com.example.demarches.service;

import com.example.demarches.dto.LieuResponse;
import com.example.demarches.model.Administration;
import com.example.demarches.model.Document;
import com.example.demarches.model.LocalisationAdm;
import com.example.demarches.model.User;
import com.example.demarches.repository.AdministrationRepository;
import com.example.demarches.repository.DocumentRepository;
import com.example.demarches.repository.LocalisationAdmRepository;
import com.example.demarches.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LieuDocumentService {

    private final DocumentRepository documentRepository;
    private final AdministrationRepository administrationRepository;
    private final LocalisationAdmRepository localisationAdmRepository;
    private final UserRepository userRepository;

    public LieuResponse getLieuPourUtilisateur(String email, Long idDocument) {
        Document document = documentRepository.findById(idDocument)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        User user = email != null
                ? userRepository.findByEmail(email).orElse(null)
                : null;

        Administration administration = determinerAdministration(document, user);
        LocalisationAdm localisation = localisationAdmRepository
                .findByAdministrationIdAdministration(administration.getIdAdministration())
                .stream().findFirst().orElse(null);

        return LieuResponse.builder()
                .idAdministration(administration.getIdAdministration())
                .libelle(administration.getLibelle())
                .adresse(localisation != null ? localisation.getAdresse() : null)
                .longitude(localisation != null ? localisation.getLongitude() : null)
                .latitude(localisation != null ? localisation.getLatitude() : null)
                .codePostal(localisation != null ? localisation.getCodePostal() : null)
                .raison(libelleRaison(document.getLibelle(), user))
                .build();
    }

    private Administration determinerAdministration(Document document, User user) {
        if (Boolean.TRUE.equals(document.getEstLieuUnique())) {
            return document.getAdministration();
        }

        if ("Certificat de résidence".equals(document.getLibelle())) {
            if (user != null && user.getFokontanyResidence() != null) {
                String fktLibelle = "Fokontany " + user.getFokontanyResidence().getLibelle();
                return administrationRepository.findByLibelle(fktLibelle)
                        .orElse(document.getAdministration());
            }
            return document.getAdministration();
        }

        return document.getAdministration();
    }

    private String libelleRaison(String documentLibelle, User user) {
        return switch (documentLibelle) {
            case "Certificat de résidence" ->
                    user != null && user.getFokontanyResidence() != null
                            ? "Lieu calculé d'après votre fokontany de résidence (« "
                              + user.getFokontanyResidence().getLibelle() + " »)"
                            : "Veuillez renseigner votre fokontany de résidence";
            case "CIN", "Acte de naissance", "Bulletin de naissance" ->
                    "Lieu calculé d'après la commune de votre lieu de naissance";
            case "Extrait de casier judiciaire B3" ->
                    "Lieu calculé d'après le tribunal du lieu de naissance";
            default -> null;
        };
    }
}