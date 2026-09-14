package com.example.demarches.repository;

import com.example.demarches.model.ProcedureMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureMereRepository extends JpaRepository<ProcedureMere, Long> {
}
