package com.example.demarches.repository;

import com.example.demarches.model.Arrondissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArrondissementRepository extends JpaRepository<Arrondissement, Long> {
    Optional<Arrondissement> findByLibelle(String libelle);
}