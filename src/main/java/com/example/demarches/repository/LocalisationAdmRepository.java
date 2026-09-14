package com.example.demarches.repository;

import com.example.demarches.model.LocalisationAdm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalisationAdmRepository extends JpaRepository<LocalisationAdm, Long> {
    List<LocalisationAdm> findByAdministrationIdAdministration(Long adminId);
}
