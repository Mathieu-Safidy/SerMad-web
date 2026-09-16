package com.example.demarches.repository;

import com.example.demarches.model.Fokontany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FokontanyRepository extends JpaRepository<Fokontany, Long> {
    Optional<Fokontany> findByLibelle(String libelle);
}