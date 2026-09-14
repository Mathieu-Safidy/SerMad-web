package com.example.demarches.repository;

import com.example.demarches.model.Entite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntiteRepository extends JpaRepository<Entite, Long> {
}
