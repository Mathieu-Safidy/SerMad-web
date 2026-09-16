package com.example.demarches.controller;

import com.example.demarches.dto.LieuResponse;
import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import com.example.demarches.service.LieuDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final ProcedureMereRepository procedureMereRepository;
    private final ProcedureFilleRepository procedureFilleRepository;
    private final LieuDocumentService lieuDocumentService;

    @GetMapping
    public ResponseEntity<List<Document>> getAll() {
        return ResponseEntity.ok(documentRepository.findAll());
    }

    @GetMapping("/lieu-unique")
    public ResponseEntity<List<Document>> getDocumentsLieuUnique() {
        return ResponseEntity.ok(documentRepository.findByEstLieuUnique(true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé")));
    }

    @GetMapping("/{id}/lieu")
    public ResponseEntity<LieuResponse> getLieu(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(lieuDocumentService.getLieuPourUtilisateur(email, id));
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
