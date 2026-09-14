package com.example.demarches.repository;

import com.example.demarches.model.Asso3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Asso3Repository extends JpaRepository<Asso3, Long> {
    List<Asso3> findByUserIdUser(Long userId);
}
