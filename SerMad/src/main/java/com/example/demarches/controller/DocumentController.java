package com.example.demarches.controller;

import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final ProcedureMereRepository procedureMereRepository;
    private final ProcedureFilleRepository procedureFilleRepository;

    @GetMapping
    public ResponseEntity<List<Document>> getAll() {
        return ResponseEntity.ok(documentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé")));
    }

    @GetMapping("/procedures")
    public ResponseEntity<List<ProcedureMere>> getProcedures() {
        return ResponseEntity.ok(procedureMereRepository.findAll());
    }

    @GetMapping("/procedures/{id}")
    public ResponseEntity<ProcedureMere> getProcedureById(@PathVariable Long id) {
        return ResponseEntity.ok(procedureMereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procédure non trouvée")));
    }
}
