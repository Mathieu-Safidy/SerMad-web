package com.example.demarches.controller;

import com.example.demarches.dto.DemandeRequest;
import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import com.example.demarches.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final DemandeService demandeService;
    private final NotificationService notificationService;
    private final QRCodeService qrCodeService;
    private final AdministrationRepository administrationRepository;
    private final ProcedureMereRepository procedureMereRepository;
    private final UserRepository userRepository;
    private final DemandeRepository demandeRepository;
    private final HistoriqueDemandeRepository historiqueDemandeRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String motDePasse,
                        RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, motDePasse)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/web/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Email ou mot de passe incorrect");
            return "redirect:/web/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/web/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Demande> demandes = demandeRepository.findAll();
        model.addAttribute("totalDemandes", demandes.size());
        model.addAttribute("enAttente", demandes.stream()
                .filter(d -> d.getStatutDemande().getLibelle().contains("attente"))
                .count());
        model.addAttribute("terminees", demandes.stream()
                .filter(d -> d.getStatutDemande().getLibelle().contains("Terminée"))
                .count());
        model.addAttribute("citoyens", userRepository.count());
        return "dashboard";
    }

    @GetMapping("/demandes")
    public String demandes(Model model,
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false) Long statut) {
        List<Demande> demandes = demandeRepository.findAll();
        model.addAttribute("demandes", demandes);
        model.addAttribute("search", search);
        return "demandes";
    }

    @GetMapping("/demandes/creer")
    public String creerDemandeForm(Model model) {
        model.addAttribute("procedures", procedureMereRepository.findAll());
        model.addAttribute("citoyens", userRepository.findAll());
        return "creer-demande";
    }

    @PostMapping("/demandes/creer")
    public String creerDemande(@RequestParam String libelle,
                               @RequestParam String reference,
                               @RequestParam(required = false) Long idProcedureMere,
                               @RequestParam Long idCitoyen,
                               RedirectAttributes redirectAttributes) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByEmail(email);

        try {
            DemandeRequest request = new DemandeRequest();
            request.setLibelle(libelle);
            request.setReference(reference);
            request.setIdProcedureMere(idProcedureMere);
            request.setIdCitoyen(idCitoyen);

            Demande demande = demandeService.creerDemande(request, user);

            notificationService.creerNotification(
                    idCitoyen,
                    "Nouvelle demande",
                    "Une nouvelle demande a ete creee: " + libelle
            );

            redirectAttributes.addFlashAttribute("success", "Demande creee avec succes");
            return "redirect:/web/demandes/" + demande.getIdDemande();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/web/demandes/creer";
        }
    }

    @GetMapping("/demandes/{id}")
    public String demandeDetail(@PathVariable Long id, Model model) {
        Demande demande = demandeService.getDemandeById(id);
        model.addAttribute("demande", demande);
        model.addAttribute("historique", demandeService.getHistorique(id));
        return "demande-detail";
    }

    @GetMapping("/demandes/{id}/qrcode")
    public String qrcode(@PathVariable Long id, Model model) {
        Demande demande = demandeService.getDemandeById(id);
        model.addAttribute("demande", demande);

        try {
            String qrCode = qrCodeService.genererQRCode(id);
            model.addAttribute("qrCodeBase64", qrCode);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur generation QR Code");
        }

        return "qrcode";
    }

    @GetMapping("/administrations")
    public String administrations(Model model) {
        model.addAttribute("administrations", administrationRepository.findAll());
        return "administrations";
    }
}
