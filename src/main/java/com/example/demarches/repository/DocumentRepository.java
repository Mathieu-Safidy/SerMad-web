package com.example.demarches.repository;

import com.example.demarches.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEstLieuUnique(Boolean estLieuUnique);
}
