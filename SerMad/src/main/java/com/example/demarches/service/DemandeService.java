package com.example.demarches.service;

import com.example.demarches.dto.DemandeRequest;
import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final ProcedureMereRepository procedureMereRepository;
    private final UserRepository userRepository;
    private final HistoriqueDemandeRepository historiqueDemandeRepository;

    public Demande creerDemande(DemandeRequest request, User agent) {
        StatutDemande statut = statutDemandeRepository.findByLibelle("En attente validation citoyen")
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));

        ProcedureMere procedureMere = procedureMereRepository.findById(request.getIdProcedureMere())
                .orElseThrow(() -> new RuntimeException("Procédure non trouvée"));

        User citoyen = userRepository.findById(request.getIdCitoyen())
                .orElseThrow(() -> new RuntimeException("Citoyen non trouvé"));

        Demande demande = Demande.builder()
                .libelle(request.getLibelle())
                .reference(request.getReference())
                .dateDebut((double) Instant.now().toEpochMilli())
                .statutDemande(statut)
                .procedureMere(procedureMere)
                .user(citoyen)
                .build();

        demande = demandeRepository.save(demande);

        HistoriqueDemande historique = HistoriqueDemande.builder()
                .demande(demande)
                .user(agent)
                .action("CREATION")
                .dateAction((double) Instant.now().toEpochMilli())
                .nouveauStatut(statut.getIdStatutDemande())
                .build();
        historiqueDemandeRepository.save(historique);

        return demande;
    }

    public List<Demande> getDemandesByUser(Long userId) {
        return demandeRepository.findByUserIdUser(userId);
    }

    public Demande getDemandeById(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
    }

    public Demande validerDemandeParCitoyen(Long demandeId, User citoyen) {
        Demande demande = getDemandeById(demandeId);

        if (!demande.getUser().getIdUser().equals(citoyen.getIdUser())) {
            throw new RuntimeException("Cette demande ne vous appartient pas");
        }

        StatutDemande statutValidee = statutDemandeRepository.findByLibelle("Validée par citoyen")
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));

        StatutDemande ancienStatut = demande.getStatutDemande();
        demande.setStatutDemande(statutValidee);
        demande = demandeRepository.save(demande);

        HistoriqueDemande historique = HistoriqueDemande.builder()
                .demande(demande)
                .user(citoyen)
                .action("VALIDATION_CITOYEN")
                .dateAction((double) Instant.now().toEpochMilli())
                .ancienStatut(ancienStatut.getIdStatutDemande())
                .nouveauStatut(statutValidee.getIdStatutDemande())
                .build();
        historiqueDemandeRepository.save(historique);

        return demande;
    }

    public List<HistoriqueDemande> getHistorique(Long demandeId) {
        return historiqueDemandeRepository.findByDemandeIdDemande(demandeId);
    }
}
