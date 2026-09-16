package com.example.demarches.repository;

import com.example.demarches.model.Administration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrationRepository extends JpaRepository<Administration, Long> {
    java.util.Optional<Administration> findByLibelle(String libelle);
}
