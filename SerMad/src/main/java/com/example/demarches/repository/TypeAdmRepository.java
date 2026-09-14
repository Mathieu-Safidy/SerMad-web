package com.example.demarches.repository;

import com.example.demarches.model.TypeAdm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeAdmRepository extends JpaRepository<TypeAdm, Long> {
    Optional<TypeAdm> findByLibelle(String libelle);
}
