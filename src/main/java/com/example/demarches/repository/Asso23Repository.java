package com.example.demarches.repository;

import com.example.demarches.model.Asso23;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Asso23Repository extends JpaRepository<Asso23, Long> {
    List<Asso23> findByUserIdUser(Long userId);
}
