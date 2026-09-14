package com.example.demarches.repository;

import com.example.demarches.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByAdministrationIdAdministration(Long adminId);
}
