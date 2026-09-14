package com.example.demarches.repository;

import com.example.demarches.model.ProcedureFille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureFilleRepository extends JpaRepository<ProcedureFille, Long> {
}
