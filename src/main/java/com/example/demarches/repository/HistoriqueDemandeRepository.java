package com.example.demarches.repository;

import com.example.demarches.model.HistoriqueDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueDemandeRepository extends JpaRepository<HistoriqueDemande, Long> {
    List<HistoriqueDemande> findByDemandeIdDemande(Long demandeId);
}
