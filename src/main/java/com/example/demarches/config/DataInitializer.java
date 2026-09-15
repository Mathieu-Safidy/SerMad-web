package com.example.demarches.config;

import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final ActionRepository actionRepository;
    private final EntiteRepository entiteRepository;
    private final Asso2Repository asso2Repository;
    private final UserRepository userRepository;
    private final Asso3Repository asso3Repository;
    private final PasswordEncoder passwordEncoder;
    private final TypeAdmRepository typeAdmRepository;
    private final AdministrationRepository administrationRepository;
    private final ServiceRepository serviceRepository;
    private final CategorieRepository categorieRepository;
    private final TypeDocumentRepository typeDocumentRepository;
    private final DocumentRepository documentRepository;
    private final ProcedureFilleRepository procedureFilleRepository;
    private final ProcedureMereRepository procedureMereRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final LocalisationAdmRepository localisationAdmRepository;
    private final DemandeRepository demandeRepository;

    @Override
    public void run(String... args) {
        log.info("=== DataInitializer : vérification des données de référence ===");

        Profile citoyen = createProfile("Citoyen");
        Profile agent = createProfile("Agent");
        Profile admin = createProfile("Admin");

        if (actionRepository.count() == 0) {
            seedRbac(agent, admin);
        } else {
            log.info("RBAC déjà en place, seed RBAC ignoré.");
        }

        seedStatuts();
        List<Administration> administrations = seedAdministrations();
        Map<String, User> users = seedUsers(citoyen, agent, admin);
        Map<String, ProcedureMere> procedures = seedDocumentsAndProcedures(administrations);
        if (demandeRepository.count() == 0 && !procedures.isEmpty() && !users.isEmpty()) {
            seedDemandes(users, procedures);
        } else {
            log.info("Demandes déjà présentes (ou référentiels manquants), création des demandes ignorée.");
        }

        log.info("=== INITIALISATION TERMINÉE ===");
        log.info("=== COMPTES DE DÉMONSTRATION ===");
        log.info("Admin:   admin@sermad.mg / admin123");
        log.info("Agent:   agent@sermad.mg / agent123");
        log.info("Citoyen: citoyen@sermad.mg / citoyen123");
        log.info("Citoyen: voahangy@sermad.mg / citoyen123");
        log.info("Citoyen: solofo@sermad.mg / citoyen123");
    }

    private void seedRbac(Profile agent, Profile admin) {
        Action view = actionRepository.save(Action.builder().libelle("VIEW").build());
        Action create = actionRepository.save(Action.builder().libelle("CREATE").build());
        Action update = actionRepository.save(Action.builder().libelle("UPDATE").build());
        Action delete = actionRepository.save(Action.builder().libelle("DELETE").build());

        Entite userEntity = createEntite("User");
        Entite demandeEntity = createEntite("Demande");
        Entite documentEntity = createEntite("Document");
        Entite administrationEntity = createEntite("Administration");
        Entite notificationEntity = createEntite("Notification");
        Entite validationEntity = createEntite("Validation");
        Entite procedureEntity = createEntite("Procedure");
        Entite serviceEntity = createEntite("Service");

        createPermission(agent, view, userEntity);
        createPermission(agent, view, demandeEntity);
        createPermission(agent, view, documentEntity);
        createPermission(agent, view, administrationEntity);
        createPermission(agent, view, notificationEntity);
        createPermission(agent, view, serviceEntity);
        createPermission(agent, create, demandeEntity);
        createPermission(agent, update, demandeEntity);

        createPermission(admin, view, userEntity);
        createPermission(admin, create, userEntity);
        createPermission(admin, update, userEntity);
        createPermission(admin, delete, userEntity);
        createPermission(admin, view, demandeEntity);
        createPermission(admin, create, demandeEntity);
        createPermission(admin, update, demandeEntity);
        createPermission(admin, delete, demandeEntity);
        createPermission(admin, view, documentEntity);
        createPermission(admin, create, documentEntity);
        createPermission(admin, view, administrationEntity);
        createPermission(admin, create, administrationEntity);
        createPermission(admin, update, administrationEntity);
        createPermission(admin, delete, administrationEntity);

        log.info("RBAC (profiles, actions, entités, permissions) inséré.");
    }

    private void seedStatuts() {
        createStatut("En attente validation citoyen");
        createStatut("Validée par citoyen");
        createStatut("En cours de traitement");
        createStatut("Terminée");
        createStatut("Rejetée");
    }

    private List<Administration> seedAdministrations() {
        if (administrationRepository.count() > 0) {
            log.info("Administrations déjà présentes, seed des administrations ignoré.");
            return administrationRepository.findAll();
        }

        TypeAdm typeCommune = createTypeAdm("Commune Urbaine");
        TypeAdm typeDistrict = createTypeAdm("District");
        TypeAdm typeFokontany = createTypeAdm("Fokontany");
        TypeAdm typeCaisse = createTypeAdm("Caisse de Prévoyance Sociale");
        TypeAdm typeImpots = createTypeAdm("Impôts");
        TypeAdm typeTribunal = createTypeAdm("Tribunal");
        TypeAdm typeMinistere = createTypeAdm("Ministère");
        TypeAdm typeIdentification = createTypeAdm("Identification");

// ============================================================
// ADMINISTRATIONS
// ============================================================

Administration cua = createAdministration(
        "Commune Urbaine d'Antananarivo (CUA)", typeCommune);

Administration district = createAdministration(
        "District d'Antananarivo-Renivohitra", typeDistrict);

Administration fktAnalakely = createAdministration(
        "Fokontany Antanimalalaka-Analakely", typeFokontany);

Administration fktAmbatovinaky = createAdministration(
        "Fokontany Ambatovinaky", typeFokontany);

Administration cnaps = createAdministration(
        "CNaPS - Caisse Nationale de Prévoyance Sociale", typeCaisse);

Administration dgi = createAdministration(
        "Direction Générale des Impôts (DGI)", typeImpots);

Administration tpi = createAdministration(
        "Tribunal de Première Instance d'Antananarivo", typeTribunal);

Administration mid = createAdministration(
        "Ministère de l'Intérieur et de la Décentralisation", typeMinistere);

Administration msp = createAdministration(
        "Ministère de la Sécurité Publique (Police Nationale)", typeMinistere);

Administration transports = createAdministration(
        "Ministère des Transports et de la Météorologie", typeMinistere);

Administration centreEnrolement = createAdministration(
        "Centre d'Enrôlement Biométrique (RECI)", typeIdentification);
// 1. CUA - Hôtel de Ville
createLocalisation(
        "Commune Urbaine d'Antananarivo (CUA)",
        "Hôtel de Ville, Avenue de l'Indépendance, Analakely, Antananarivo 101",
        -18.905227,
        47.523749,
        "101",
        cua
);

// 2. District Antananarivo-Renivohitra
createLocalisation(
        "District d'Antananarivo-Renivohitra",
        "Analakely, Antananarivo 101",
        -18.9065,
        47.5245,
        "101",
        district
);

// 3. Fokontany Antanimalalaka-Analakely
createLocalisation(
        "Fokontany Antanimalalaka-Analakely",
        "Antanimalalaka, Analakely, Antananarivo 101",
        -18.9060,
        47.5240,
        "101",
        fktAnalakely
);

// 4. Fokontany Ambatovinaky
createLocalisation(
        "Fokontany Ambatovinaky",
        "Ambatovinaky, Antananarivo 101",
        -18.9115,
        47.5235,
        "101",
        fktAmbatovinaky
);

// 5. CNaPS
createLocalisation(
        "CNaPS - Caisse Nationale de Prévoyance Sociale",
        "Place Ho Chi Minh, Ampefiloha, Antananarivo 101",
        -18.9110,
        47.5175,
        "101",
        cnaps
);

// 6. DGI
createLocalisation(
        "Direction Générale des Impôts (DGI)",
        "Immeuble MFB, Antaninarenina, Antananarivo 101",
        -18.9103,
        47.5250,
        "101",
        dgi
);

// 7. Tribunal de Première Instance
createLocalisation(
        "Tribunal de Première Instance d'Antananarivo",
        "Palais de Justice, Anosy, Antananarivo 101",
        -18.9165,
        47.5191,
        "101",
        tpi
);

// 8. Ministère de l'Intérieur
createLocalisation(
        "Ministère de l'Intérieur et de la Décentralisation",
        "Rue Lamyne Gueye, Anosy, Antananarivo 101",
        -18.9150,
        47.5200,
        "101",
        mid
);

// 9. Ministère de la Sécurité Publique
createLocalisation(
        "Ministère de la Sécurité Publique (Police Nationale)",
        "Complexe Patte d'Éléphant, Anosy, Antananarivo 101",
        -18.9155,
        47.5210,
        "101",
        msp
);

// 10. Ministère des Transports
createLocalisation(
        "Ministère des Transports et de la Météorologie",
        "Cité Ampefiloha, Antananarivo 101",
        -18.91741,
        47.51945,
        "101",
        transports
);

        createService("État Civil (actes de naissance, mariage, décès)", cua);
        createService("Urbanisme et domaines", cua);
        createService("Identification et carte nationale d'identité (CNI)", district);
        createService("Certificat de résidence", fktAnalakely);
        createService("Actes administratifs du fokontany", fktAmbatovinaky);
        createService("Prestations sociales (allocations familiales, retraites)", cnaps);
        createService("Fiscalité des particuliers", dgi);
        createService("Immatriculation fiscale (NIF)", dgi);
        createService("Greffe, état civil judiciaire et casier judiciaire", tpi);
        createService("Passeports - Contrôle de l'immigration et de l'émigration", msp);
        createService("Bureau des permis de conduire", transports);
        createService("Enrôlement biométrique et numéro d'identification", centreEnrolement);

        log.info("{} administrations insérées (Antananarivo).", administrationRepository.count());
        return administrationRepository.findAll();
    }

    private Map<String, User> seedUsers(Profile citoyen, Profile agent, Profile admin) {
        Map<String, User> users = new HashMap<>();
        if (userRepository.count() > 0) {
            log.info("Des utilisateurs existent déjà, comptes de démonstration ignorés.");
            userRepository.findAll().forEach(u -> users.put(u.getEmail(), u));
            return users;
        }

        User adminUser = createUser("Admin", "SerMad", "admin@sermad.mg", "admin123", "0340000001", "107450150001");
        User agentUser = createUser("Rakotomalala", "Tahiana", "agent@sermad.mg", "agent123", "0340000002", "107450150002");
        User citoyenUser = createUser("Rakoto", "Nomeny", "citoyen@sermad.mg", "citoyen123", "0340000003", "107450150003");
        User citoyen2 = createUser("Rasoa", "Voahangy", "voahangy@sermad.mg", "citoyen123", "0340000004", "107450150004");
        User citoyen3 = createUser("Rabe", "Solofo", "solofo@sermad.mg", "citoyen123", "0340000005", "107450150005");

        assignProfile(adminUser, admin);
        assignProfile(agentUser, agent);
        assignProfile(citoyenUser, citoyen);
        assignProfile(citoyen2, citoyen);
        assignProfile(citoyen3, citoyen);

        users.put(adminUser.getEmail(), adminUser);
        users.put(agentUser.getEmail(), agentUser);
        users.put(citoyenUser.getEmail(), citoyenUser);
        users.put(citoyen2.getEmail(), citoyen2);
        users.put(citoyen3.getEmail(), citoyen3);

        log.info("Comptes de démonstration insérés.");
        return users;
    }

    private Map<String, ProcedureMere> seedDocumentsAndProcedures(List<Administration> administrations) {
        Map<String, ProcedureMere> procedures = new HashMap<>();
        if (documentRepository.count() > 0) {
            log.info("Documents déjà présents, seed des documents ignoré.");
            return procedures;
        }

        Categorie catEtatCivil = createCategorie("État civil");
        Categorie catIdentite = createCategorie("Identité");
        Categorie catResidence = createCategorie("Résidence et domaine");
        Categorie catFiscal = createCategorie("Fiscal");
        Categorie catJustice = createCategorie("Justice");
        Categorie catSocial = createCategorie("Sécurité sociale");
        Categorie catDeplacements = createCategorie("Déplacements");

        TypeDocument typeActeNaissance = createTypeDocument("Acte de naissance");
        TypeDocument typeCNI = createTypeDocument("Carte nationale d'identité");
        TypeDocument typePasseport = createTypeDocument("Passeport");
        TypeDocument typePermis = createTypeDocument("Permis de conduire");
        TypeDocument typeJusticDomicile = createTypeDocument("Justificatif de résidence");
        TypeDocument typeNationalite = createTypeDocument("Attestation de nationalité");
        TypeDocument typeCasier = createTypeDocument("Extrait de casier judiciaire");
        TypeDocument typeCertificatFiscal = createTypeDocument("Certificat fiscal");
        TypeDocument typeAttestSocial = createTypeDocument("Attestation de sécurité sociale");
        TypeDocument typeCarteGrise = createTypeDocument("Carte grise");

        Document docExtraitNaissance = createDocument("Extrait d'acte de naissance", 0, catEtatCivil, adm(administrations, "Commune Urbaine d'Antananarivo (CUA)"), typeActeNaissance);
        Document docCopieNaissance = createDocument("Copie intégrale d'acte de naissance", 0, catEtatCivil, adm(administrations, "Commune Urbaine d'Antananarivo (CUA)"), typeActeNaissance);
        Document docCNI = createDocument("Carte nationale d'identité (CNI)", 18, catIdentite, adm(administrations, "District d'Antananarivo-Renivohitra"), typeCNI);
        Document docCertifResidence = createDocument("Certificat de résidence", 0, catResidence, adm(administrations, "Fokontany Antanimalalaka-Analakely"), typeJusticDomicile);
        Document docAttDomicile = createDocument("Attestation de domicile", 0, catResidence, adm(administrations, "Fokontany Ambatovinaky"), typeJusticDomicile);
        Document docPasseport = createDocument("Passeport ordinaire malagasy", 0, catIdentite, adm(administrations, "Ministère de la Sécurité Publique (Police Nationale)"), typePasseport);
        Document docCasier = createDocument("Extrait de casier judiciaire (bulletin n°3)", 0, catJustice, adm(administrations, "Tribunal de Première Instance d'Antananarivo"), typeCasier);
        Document docNationalite = createDocument("Certificat de nationalité", 0, catJustice, adm(administrations, "Tribunal de Première Instance d'Antananarivo"), typeNationalite);
        Document docPermis = createDocument("Permis de conduire (catégorie B)", 18, catDeplacements, adm(administrations, "Ministère des Transports et de la Météorologie"), typePermis);
        Document docCarteGrise = createDocument("Carte grise (certificat d'immatriculation)", 18, catDeplacements, adm(administrations, "Ministère des Transports et de la Météorologie"), typeCarteGrise);
        Document docNIF = createDocument("NIF - attestation d'immatriculation fiscale", 0, catFiscal, adm(administrations, "Direction Générale des Impôts (DGI)"), typeCertificatFiscal);
        Document docQuittance = createDocument("Quittance de non-imposition", 0, catFiscal, adm(administrations, "Direction Générale des Impôts (DGI)"), typeCertificatFiscal);
        Document docAffiliation = createDocument("Attestation d'affiliation à la CNaPS", 0, catSocial, adm(administrations, "CNaPS - Caisse Nationale de Prévoyance Sociale"), typeAttestSocial);

        procedures.put(docExtraitNaissance.getLibelle(), createProcedureMere(docExtraitNaissance, 3.0, 2000.0));
        procedures.put(docCopieNaissance.getLibelle(), createProcedureMere(docCopieNaissance, 5.0, 5000.0));
        procedures.put(docCNI.getLibelle(), createProcedureMere(docCNI, 30.0, 200.0));
        procedures.put(docCertifResidence.getLibelle(), createProcedureMere(docCertifResidence, 1.0, 2000.0));
        procedures.put(docAttDomicile.getLibelle(), createProcedureMere(docAttDomicile, 1.0, 1000.0));
        procedures.put(docPasseport.getLibelle(), createProcedureMere(docPasseport, 30.0, 150000.0));
        procedures.put(docCasier.getLibelle(), createProcedureMere(docCasier, 5.0, 8000.0));
        procedures.put(docNationalite.getLibelle(), createProcedureMere(docNationalite, 15.0, 10000.0));
        procedures.put(docPermis.getLibelle(), createProcedureMere(docPermis, 20.0, 150000.0));
        procedures.put(docCarteGrise.getLibelle(), createProcedureMere(docCarteGrise, 15.0, 100000.0));
        procedures.put(docNIF.getLibelle(), createProcedureMere(docNIF, 7.0, 0.0));
        procedures.put(docQuittance.getLibelle(), createProcedureMere(docQuittance, 5.0, 0.0));
        procedures.put(docAffiliation.getLibelle(), createProcedureMere(docAffiliation, 7.0, 0.0));

        log.info("{} documents et procédures insérés.", documentRepository.count());
        return procedures;
    }

    private void seedDemandes(Map<String, User> users, Map<String, ProcedureMere> procedures) {
        User citoyenUser = users.get("citoyen@sermad.mg");
        User citoyen2 = users.get("voahangy@sermad.mg");
        User citoyen3 = users.get("solofo@sermad.mg");

        createDemande("Demande d'extrait d'acte de naissance", "DEM-MG-001",
                statut("En attente validation citoyen"), procedures.get("Extrait d'acte de naissance"), citoyenUser);
        createDemande("Demande de carte nationale d'identité", "DEM-MG-002",
                statut("Validée par citoyen"), procedures.get("Carte nationale d'identité (CNI)"), citoyenUser);
        createDemande("Demande de passeport ordinaire", "DEM-MG-003",
                statut("En cours de traitement"), procedures.get("Passeport ordinaire malagasy"), citoyen2);
        createDemande("Demande de permis de conduire B", "DEM-MG-004",
                statut("En attente validation citoyen"), procedures.get("Permis de conduire (catégorie B)"), citoyen3);
        createDemande("Quittance de non-imposition 2026", "DEM-MG-005",
                statut("Terminée"), procedures.get("Quittance de non-imposition"), citoyenUser);

        log.info("Demandes d'exemple insérées.");
    }

    private Profile createProfile(String nom) {
        return profileRepository.findByNom(nom)
                .orElseGet(() -> profileRepository.save(Profile.builder().nom(nom).build()));
    }

    private Entite createEntite(String libelle) {
        return entiteRepository.save(Entite.builder().libelle(libelle).build());
    }

    private void createPermission(Profile profile, Action action, Entite entite) {
        asso2Repository.save(Asso2.builder()
                .profile(profile).action(action).entite(entite).build());
    }

    private StatutDemande statut(String libelle) {
        return statutDemandeRepository.findByLibelle(libelle).orElse(null);
    }

    private void createStatut(String libelle) {
        statutDemandeRepository.findByLibelle(libelle)
                .orElseGet(() -> statutDemandeRepository.save(StatutDemande.builder().libelle(libelle).build()));
    }

    private TypeAdm createTypeAdm(String libelle) {
        return typeAdmRepository.findByLibelle(libelle)
                .orElseGet(() -> typeAdmRepository.save(TypeAdm.builder().libelle(libelle).build()));
    }

    private Administration createAdministration(String libelle, TypeAdm type) {
        return administrationRepository.save(Administration.builder().libelle(libelle).typeAdm(type).build());
    }

    private void createLocalisation(String libelle, String adresse, Double lat, Double lon, String cp, Administration adm) {
        localisationAdmRepository.save(LocalisationAdm.builder()
                .libelle(libelle).adresse(adresse).latitude(lat).longitude(lon).codePostal(cp).administration(adm).build());
    }

    private Service createService(String nom, Administration adm) {
        return serviceRepository.save(Service.builder().nom(nom).administration(adm).build());
    }

    private Categorie createCategorie(String libelle) {
        return categorieRepository.findByLibelle(libelle)
                .orElseGet(() -> categorieRepository.save(Categorie.builder().libelle(libelle).build()));
    }

    private TypeDocument createTypeDocument(String libelle) {
        return typeDocumentRepository.findByLibelle(libelle)
                .orElseGet(() -> typeDocumentRepository.save(TypeDocument.builder().libelle(libelle).build()));
    }

    private Document createDocument(String libelle, Integer ageMin, Categorie cat, Administration adm, TypeDocument type) {
        return documentRepository.save(Document.builder()
                .libelle(libelle).ageMinimum(ageMin).categorie(cat).administration(adm).typeDocument(type).build());
    }

    private ProcedureMere createProcedureMere(Document doc, Double delai, Double cout) {
        ProcedureFille pf = procedureFilleRepository.save(ProcedureFille.builder().delai(delai).cout(cout).build());
        return procedureMereRepository.save(ProcedureMere.builder().document(doc).procedureFille(pf).build());
    }

    private User createUser(String nom, String prenom, String email, String mdp, String tel, String cin) {
        return userRepository.save(User.builder()
                .nom(nom).prenom(prenom).email(email).telephone(tel)
                .motDePasse(passwordEncoder.encode(mdp)).cin(cin).estActif(true).build());
    }

    private void assignProfile(User user, Profile profile) {
        asso3Repository.save(Asso3.builder().user(user).profile(profile).build());
    }

    private void createDemande(String libelle, String reference, StatutDemande statut, ProcedureMere pm, User user) {
        if (statut == null || pm == null || user == null) {
            log.warn("Demande '{}' ignorée : référentiel incomplet.", libelle);
            return;
        }
        Demande demande = Demande.builder()
                .libelle(libelle)
                .reference(reference)
                .dateDebut((double) System.currentTimeMillis())
                .statutDemande(statut)
                .procedureMere(pm)
                .user(user)
                .build();
        demandeRepository.save(demande);
    }

    private Administration adm(List<Administration> administrations, String libelle) {
        return administrations.stream()
                .filter(a -> a.getLibelle().equals(libelle))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Administration introuvable : " + libelle));
    }
}