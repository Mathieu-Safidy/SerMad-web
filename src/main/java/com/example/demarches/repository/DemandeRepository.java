package com.example.demarches.repository;

import com.example.demarches.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByUserIdUser(Long userId);
    List<Demande> findByStatutDemandeIdStatutDemande(Long statutId);
}
