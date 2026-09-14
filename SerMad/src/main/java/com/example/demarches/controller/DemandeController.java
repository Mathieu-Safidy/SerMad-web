package com.example.demarches.controller;

import com.example.demarches.dto.DemandeRequest;
import com.example.demarches.model.*;
import com.example.demarches.service.AuthService;
import com.example.demarches.service.DemandeService;
import com.example.demarches.service.NotificationService;
import com.example.demarches.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;
    private final NotificationService notificationService;
    private final QRCodeService qrCodeService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> creerDemande(
            @RequestBody DemandeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User agent = authService.getUserByEmail(userDetails.getUsername());
        Demande demande = demandeService.creerDemande(request, agent);

        notificationService.creerNotification(
                demande.getUser().getIdUser(),
                "Nouvelle demande",
                "Une nouvelle demande a été créée pour vous: " + demande.getLibelle()
        );

        String qrCodeBase64 = qrCodeService.genererQRCode(demande.getIdDemande());

        return ResponseEntity.ok(Map.of(
                "demande", demande,
                "qrCode", qrCodeBase64
        ));
    }

    @GetMapping
    public ResponseEntity<List<Demande>> getDemandes(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = authService.getUserByEmail(userDetails.getUsername());
        List<Demande> demandes = demandeService.getDemandesByUser(user.getIdUser());
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemande(@PathVariable Long id) {
        return ResponseEntity.ok(demandeService.getDemandeById(id));
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<Demande> validerDemande(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User citoyen = authService.getUserByEmail(userDetails.getUsername());
        Demande demande = demandeService.validerDemandeParCitoyen(id, citoyen);
        return ResponseEntity.ok(demande);
    }

    @GetMapping("/{id}/historique")
    public ResponseEntity<List<HistoriqueDemande>> getHistorique(@PathVariable Long id) {
        return ResponseEntity.ok(demandeService.getHistorique(id));
    }

    @PostMapping("/qrcode/valider")
    public ResponseEntity<Map<String, Boolean>> validerQRCode(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean valide = qrCodeService.validerQRCode(token);
        return ResponseEntity.ok(Map.of("valide", valide));
    }
}
