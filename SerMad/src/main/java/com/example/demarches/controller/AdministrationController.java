package com.example.demarches.controller;

import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrations")
@RequiredArgsConstructor
public class AdministrationController {

    private final AdministrationRepository administrationRepository;
    private final LocalisationAdmRepository localisationAdmRepository;
    private final ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity<List<Administration>> getAll() {
        return ResponseEntity.ok(administrationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administration> getById(@PathVariable Long id) {
        return ResponseEntity.ok(administrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administration non trouvée")));
    }

    @GetMapping("/{id}/localisation")
    public ResponseEntity<List<LocalisationAdm>> getLocalisation(@PathVariable Long id) {
        return ResponseEntity.ok(localisationAdmRepository.findByAdministrationIdAdministration(id));
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<List<Service>> getServices(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRepository.findByAdministrationIdAdministration(id));
    }
}
