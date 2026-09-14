package com.example.demarches.repository;

import com.example.demarches.model.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Long> {
    Optional<StatutDemande> findByLibelle(String libelle);
}
