package com.example.demarches.repository;

import com.example.demarches.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {
    List<Validation> findByDemandeIdDemande(Long demandeId);
}
