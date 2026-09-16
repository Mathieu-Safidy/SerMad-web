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
    private final CommuneRepository communeRepository;
    private final ArrondissementRepository arrondissementRepository;
    private final FokontanyRepository fokontanyRepository;

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
        seedGeographie();
        List<Administration> administrations = seedAdministrations();
        seedFokontanyAdministrations();
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
        TypeAdm typeImmatriculation = createTypeAdm("Immatriculation");

// ============================================================
// ADMINISTRATIONS
// ============================================================

Administration cua = createAdministration(
        "Commune Urbaine d'Antananarivo (CUA)", typeCommune);

Administration district = createAdministration(
        "District d'Antananarivo-Renivohitra", typeDistrict);

Administration fktAnalakely = createAdministration(
        "Fokontany Antanimalalaka", typeFokontany);

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

Administration centreImmatriculation = createAdministration(
        "Centre d'Immatriculation Anosy", typeImmatriculation);
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

// 3. Fokontany Antanimalalaka
createLocalisation(
        "Fokontany Antanimalalaka",
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

// 11. Centre d'Immatriculation Anosy
createLocalisation(
        "Centre d'Immatriculation Anosy",
        "Rue Rasoamanarivo, Anosy, Antananarivo 101",
        -18.9162,
        47.5198,
        "101",
        centreImmatriculation
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
        createService("Immatriculation des véhicules et permis de conduire", centreImmatriculation);

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

        User adminUser = createUser(
                "Admin", "SerMad", "admin@sermad.mg", "admin123", "0340000001", "107450150001",
                "1990-01-15", "Antananarivo, Analakely", "Malagasy",
                "Rakotoarimanana", "Jean-Pierre", "Razafinjatovo", "Marie-Claire",
                "Lot II G 12, Rue Rainilaiarivony, Analakely", fkt("Antanimalalaka"));
        User agentUser = createUser(
                "Rakotomalala", "Tahiana", "agent@sermad.mg", "agent123", "0340000002", "107450150002",
                "1985-06-28", "Antananarivo, Antsahabe", "Malagasy",
                "Rakotovao", "Ferdinand", "Rasoa", "Céline",
                "Lot II M 45, Antsahabe", fkt("Antsahabe"));
        User citoyenUser = createUser(
                "Rakoto", "Nomeny", "citoyen@sermad.mg", "citoyen123", "0340000003", "107450150003",
                "1995-03-20", "Antananarivo, Ambohijatovo", "Malagasy",
                "Rakotoarisoa", "Lucien", "Razanadrakoto", "Bako",
                "Lot II H 89 bis, Ambohijatovo Fara", fkt("Ambohijatovo Fara"));
        User citoyen2 = createUser(
                "Rasoa", "Voahangy", "voahangy@sermad.mg", "citoyen123", "0340000004", "107450150004",
                "1998-11-05", "Antananarivo, Analamahitsy", "Malagasy",
                "Rasoamanarivo", "Hery", "Razafindrakoto", "Lalao",
                "Lot II T 23, Analamahitsy", fkt("Analamahitsy"));
        User citoyen3 = createUser(
                "Rabe", "Solofo", "solofo@sermad.mg", "citoyen123", "0340000005", "107450150005",
                "1988-07-14", "Antananarivo, Soavimasoandro", "Malagasy",
                "Rabenjamina", "Albert", "Razanamparany", "Julienne",
                "Lot II D 77, Soavimasoandro", fkt("Soavimasoandro"));

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
            log.info("Documents déjà présents, mise à jour des champs manquants (estLieuUnique, dossier).");
            backfillDocuments();
            return procedures;
        }

        Categorie catEtatCivil = createCategorie("État civil");
        Categorie catIdentite = createCategorie("Identité");
        Categorie catResidence = createCategorie("Résidence et domaine");
        Categorie catFiscal = createCategorie("Fiscal");
        Categorie catJustice = createCategorie("Justice");
        Categorie catDeplacements = createCategorie("Déplacements");

        TypeDocument typeActeNaissance = createTypeDocument("Acte de naissance");
        TypeDocument typeCNI = createTypeDocument("Carte nationale d'identité");
        TypeDocument typePermis = createTypeDocument("Permis de conduire");
        TypeDocument typeJusticDomicile = createTypeDocument("Justificatif de résidence");
        TypeDocument typeCasier = createTypeDocument("Extrait de casier judiciaire");
        TypeDocument typeCertificatFiscal = createTypeDocument("Certificat fiscal");

        Document docActeNaissance = createDocument("Acte de naissance", 0, false, catEtatCivil, adm(administrations, "Commune Urbaine d'Antananarivo (CUA)"), typeActeNaissance, """
                Nom, prénom, date de naissance
                Lieu de naissance
                Nom et prénom du père
                Nom et prénom de la mère
                Photocopie de la pièce d'identité
                Formulaire de demande
                Frais de dossier: 2 000 Ar
                """);
        Document docCIN = createDocument("CIN", 18, false, catIdentite, adm(administrations, "Commune Urbaine d'Antananarivo (CUA)"), typeCNI, """
                Nom, prénom, date de naissance
                Lieu de naissance
                Adresse de résidence, fokontany
                Extrait d'acte de naissance
                2 photos d'identité
                Formulaire de demande
                Taxe: 200 Ar
                """);
        Document docPermis = createDocument("Permis de conduire", 18, true, catDeplacements, adm(administrations, "Centre d'Immatriculation Anosy"), typePermis, """
                Carte nationale d'identité
                Certificat médical
                2 photos d'identité
                Formulaire de demande
                Frais de dossier: 150 000 Ar
                """);
        Document docCertifResidence = createDocument("Certificat de résidence", 0, false, catResidence, adm(administrations, "Fokontany Antanimalalaka"), typeJusticDomicile, """
                Pièce d'identité
                Formulaire de demande
                Certificat d'inhabitation du fokontany
                """);
        Document docBulletinNaissance = createDocument("Bulletin de naissance", 0, false, catEtatCivil, adm(administrations, "Commune Urbaine d'Antananarivo (CUA)"), typeActeNaissance, """
                Nom, prénom, date de naissance
                Lieu de naissance
                Nom et prénom du père
                Nom et prénom de la mère
                Photocopie de la pièce d'identité
                Formulaire de demande de bulletin
                Frais de dossier: 5 000 Ar
                """);
        Document docCasier = createDocument("Extrait de casier judiciaire B3", 0, false, catJustice, adm(administrations, "Tribunal de Première Instance d'Antananarivo"), typeCasier, """
                Carte nationale d'identité
                Formulaire de demande
                Frais de dossier: 8 000 Ar
                """);
        Document docImpots = createDocument("Impôts", 0, true, catFiscal, adm(administrations, "Direction Générale des Impôts (DGI)"), typeCertificatFiscal, """
                Carte nationale d'identité
                NIF - attestation d'immatriculation fiscale
                Formulaire de demande
                """);

        procedures.put(docActeNaissance.getLibelle(), createProcedureMere(docActeNaissance, 3.0, 2000.0));
        procedures.put(docCIN.getLibelle(), createProcedureMere(docCIN, 30.0, 200.0));
        procedures.put(docPermis.getLibelle(), createProcedureMere(docPermis, 20.0, 150000.0));
        procedures.put(docCertifResidence.getLibelle(), createProcedureMere(docCertifResidence, 1.0, 2000.0));
        procedures.put(docBulletinNaissance.getLibelle(), createProcedureMere(docBulletinNaissance, 5.0, 5000.0));
        procedures.put(docCasier.getLibelle(), createProcedureMere(docCasier, 5.0, 8000.0));
        procedures.put(docImpots.getLibelle(), createProcedureMere(docImpots, 7.0, 0.0));

        log.info("{} documents et procédures insérés.", documentRepository.count());
        return procedures;
    }

    private void seedDemandes(Map<String, User> users, Map<String, ProcedureMere> procedures) {
        User citoyenUser = users.get("citoyen@sermad.mg");
        User citoyen2 = users.get("voahangy@sermad.mg");
        User citoyen3 = users.get("solofo@sermad.mg");

        createDemande("Demande d'acte de naissance", "DEM-MG-001",
                statut("En attente validation citoyen"), procedures.get("Acte de naissance"), citoyenUser);
        createDemande("Demande de CIN", "DEM-MG-002",
                statut("Validée par citoyen"), procedures.get("CIN"), citoyenUser);
        createDemande("Demande de permis de conduire", "DEM-MG-003",
                statut("En cours de traitement"), procedures.get("Permis de conduire"), citoyen2);
        createDemande("Demande de certificat de résidence", "DEM-MG-004",
                statut("En attente validation citoyen"), procedures.get("Certificat de résidence"), citoyen3);
        createDemande("Demande d'extrait de casier judiciaire B3", "DEM-MG-005",
                statut("Terminée"), procedures.get("Extrait de casier judiciaire B3"), citoyenUser);

        log.info("Demandes d'exemple insérées.");
    }

    private void backfillDocuments() {
        Map<String, Boolean> lieuUniqueParLibelle = Map.ofEntries(
                Map.entry("Acte de naissance", false),
                Map.entry("CIN", false),
                Map.entry("Permis de conduire", true),
                Map.entry("Certificat de résidence", false),
                Map.entry("Bulletin de naissance", false),
                Map.entry("Extrait de casier judiciaire B3", false),
                Map.entry("Impôts", true)
        );

        Map<String, String> dossierParLibelle = Map.ofEntries(
                Map.entry("Acte de naissance", """
                        Nom, prénom, date de naissance
                        Lieu de naissance
                        Nom et prénom du père
                        Nom et prénom de la mère
                        Photocopie de la pièce d'identité
                        Formulaire de demande
                        Frais de dossier: 2 000 Ar
                        """),
                Map.entry("CIN", """
                        Nom, prénom, date de naissance
                        Lieu de naissance
                        Adresse de résidence, fokontany
                        Extrait d'acte de naissance
                        2 photos d'identité
                        Formulaire de demande
                        Taxe: 200 Ar
                        """),
                Map.entry("Permis de conduire", """
                        Carte nationale d'identité
                        Certificat médical
                        2 photos d'identité
                        Formulaire de demande
                        Frais de dossier: 150 000 Ar
                        """),
                Map.entry("Certificat de résidence", """
                        Pièce d'identité
                        Formulaire de demande
                        Certificat d'inhabitation du fokontany
                        """),
                Map.entry("Bulletin de naissance", """
                        Nom, prénom, date de naissance
                        Lieu de naissance
                        Nom et prénom du père
                        Nom et prénom de la mère
                        Photocopie de la pièce d'identité
                        Formulaire de demande de bulletin
                        Frais de dossier: 5 000 Ar
                        """),
                Map.entry("Extrait de casier judiciaire B3", """
                        Carte nationale d'identité
                        Formulaire de demande
                        Frais de dossier: 8 000 Ar
                        """),
                Map.entry("Impôts", """
                        Carte nationale d'identité
                        NIF - attestation d'immatriculation fiscale
                        Formulaire de demande
                        """)
        );

        documentRepository.findAll().forEach(doc -> {
            boolean modifie = false;
            Boolean lieuUnique = lieuUniqueParLibelle.get(doc.getLibelle());
            if (lieuUnique != null && doc.getEstLieuUnique() == null) {
                doc.setEstLieuUnique(lieuUnique);
                modifie = true;
            }
            String dossier = dossierParLibelle.get(doc.getLibelle());
            if (dossier != null && doc.getDossier() == null) {
                doc.setDossier(dossier);
                modifie = true;
            }
            if (modifie) {
                documentRepository.save(doc);
                log.info("Document '{}' mis à jour (estLieuUnique/dossier).", doc.getLibelle());
            }
        });
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

    private Document createDocument(String libelle, Integer ageMin, Boolean estLieuUnique, Categorie cat, Administration adm, TypeDocument type, String dossier) {
        return documentRepository.save(Document.builder()
                .libelle(libelle).ageMinimum(ageMin).estLieuUnique(estLieuUnique)
                .dossier(dossier)
                .categorie(cat).administration(adm).typeDocument(type).build());
    }

    private ProcedureMere createProcedureMere(Document doc, Double delai, Double cout) {
        ProcedureFille pf = procedureFilleRepository.save(ProcedureFille.builder().delai(delai).cout(cout).build());
        return procedureMereRepository.save(ProcedureMere.builder().document(doc).procedureFille(pf).build());
    }

    private User createUser(String nom, String prenom, String email, String mdp, String tel, String cin,
                                String dateNaissance, String lieuNaissance, String nationalite,
                                String nomPere, String prenomPere, String nomMere, String prenomMere,
                                String adresseResidence, Fokontany fokontanyResidence) {
        return userRepository.save(User.builder()
                .nom(nom).prenom(prenom).email(email).telephone(tel)
                .motDePasse(passwordEncoder.encode(mdp)).cin(cin)
                .dateNaissance(dateNaissance)
                .lieuNaissance(lieuNaissance)
                .nationalite(nationalite)
                .nomPere(nomPere).prenomPere(prenomPere)
                .nomMere(nomMere).prenomMere(prenomMere)
                .adresseResidence(adresseResidence)
                .fokontanyResidence(fokontanyResidence)
                .estActif(true).build());
    }

    private void assignProfile(User user, Profile profile) {
        asso3Repository.save(Asso3.builder().user(user).profile(profile).build());
    }

    private void seedGeographie() {
        if (communeRepository.count() > 0) {
            log.info("Géographie (commune, arrondissements, fokontany) déjà en place, seed ignoré.");
            return;
        }

        Commune cua = createCommune("Commune Urbaine d'Antananarivo");

        Arrondissement ar1 = createArrondissement("1er arrondissement", cua);
        Arrondissement ar2 = createArrondissement("2e arrondissement", cua);
        Arrondissement ar3 = createArrondissement("3e arrondissement", cua);
        Arrondissement ar4 = createArrondissement("4e arrondissement", cua);
        Arrondissement ar5 = createArrondissement("5e arrondissement", cua);
        Arrondissement ar6 = createArrondissement("6e arrondissement", cua);

// ===== 1er arrondissement =====
        createFokontany("Antanimalalaka", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ambalavao Isotry", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ambatonakanga", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ambohitsorohitra", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ambatovinaky", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Soarano Ambondrona", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ambodifilao", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ampandrana Ankadivato", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Amparibe Ambohidahy", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Mahamasina", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Amboasarikely", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ambatomena", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Anatihazo Isotry", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Andohatapenaka", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Antohomadinika", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Cité 67 Ha", ar1, "Commune Rurale d'Ambohidratrimo");
        createFokontany("Ankasina", ar1, "Commune Rurale d'Ambohidratrimo");

// ===== 2e arrondissement =====
        createFokontany("Faliarivo Ambanidia", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Antanimora Ampasanimalo", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Antsahabe", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Ankorahotra Ankazoto", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Ambohitsiroa", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Ankazotokana Ambony", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Tsiadana", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Volosarika Ambanidia", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Andafiavaratra", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Ambavahadimitafo", ar2, "Commune Rurale d'Androhibe");
        createFokontany("Andohamandry", ar2, "Commune Rurale d'Androhibe");

// ===== 3e arrondissement =====
        createFokontany("Antaninandro", ar3, "Commune Rurale d'Ambohimangakely");
        createFokontany("Ambohijanahary", ar3, "Commune Rurale d'Ambohimangakely");
        createFokontany("Ankorondrano Andranomahery", ar3, "Commune Rurale d'Ambohimangakely");
        createFokontany("Ankorondrano Atsinanana", ar3, "Commune Rurale d'Ambohimangakely");
        createFokontany("Ankorondrano Andrefana", ar3, "Commune Rurale d'Ambohimangakely");
        createFokontany("Ankazomanga Andraharo", ar3, "Commune Rurale d'Ambohimangakely");
        createFokontany("Ambodivona Ankadifotsy", ar3, "Commune Rurale d'Ambohimangakely");

// ===== 4e arrondissement =====
        createFokontany("Ampefiloha", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Ampasika", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Amboniloha", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Ambilanibe", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Ambohijatovo Fara", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Andrefan'Ambohijanahary", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Anosizato Est", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Anosipatrana Est", ar4, "Commune Rurale d'Ambohitrimanjaka");
        createFokontany("Ampefiloha Ambodirano", ar4, "Commune Rurale d'Ambohitrimanjaka");

// ===== 5e arrondissement =====
        createFokontany("Soavimasoandro", ar5, "Commune Rurale d'Alasora");
        createFokontany("Morarano Alarobia", ar5, "Commune Rurale d'Alasora");
        createFokontany("Anjezika", ar5, "Commune Rurale d'Alasora");

// ===== 6e arrondissement =====
        createFokontany("Andraharo", ar6, "Commune Rurale d'Ankazondandy");
        createFokontany("Ambodivonkely", ar6, "Commune Rurale d'Ankazondandy");
        createFokontany("Avaratetezana", ar6, "Commune Rurale d'Ankazondandy");
        createFokontany("Antanjombe Avaratra", ar6, "Commune Rurale d'Ankazondandy");
        createFokontany("Ampefiloha Ankeniheny", ar6, "Commune Rurale d'Ankazondandy");
        createFokontany("Analamahitsy", ar6, "Commune Rurale d'Ankazondandy");

        log.info("Géographie (1 commune, {} arrondissements, {} fokontany) insérée.",
                arrondissementRepository.count(), fokontanyRepository.count());
    }

    private Fokontany fkt(String libelle) {
        return fokontanyRepository.findByLibelle(libelle)
                .orElseThrow(() -> new IllegalStateException("Fokontany introuvable : " + libelle));
    }

    private void seedFokontanyAdministrations() {
        TypeAdm typeFokontany = typeAdmRepository.findByLibelle("Fokontany")
                .orElseGet(() -> typeAdmRepository.save(TypeAdm.builder().libelle("Fokontany").build()));

        List<Fokontany> fokontanys = fokontanyRepository.findAll();
        if (fokontanys.isEmpty()) {
            log.info("Aucun fokontany, administration de fokontany ignorée.");
            return;
        }

        Map<String, Integer> indexParArrondissement = new HashMap<>();
        int created = 0;
        for (Fokontany fkt : fokontanys) {
            String adminLibelle = "Fokontany " + fkt.getLibelle();
            if (administrationRepository.findByLibelle(adminLibelle).isPresent()) {
                continue;
            }

            String arrondissementLibelle = fkt.getArrondissement().getLibelle();
            int index = indexParArrondissement.getOrDefault(arrondissementLibelle, 0);
            indexParArrondissement.put(arrondissementLibelle, index + 1);

            double[] centre = centreArrondissement(arrondissementLibelle);
            double latitude = centre[0] + (index % 3) * 0.0038;
            double longitude = centre[1] + ((index / 3) % 3) * 0.0034 - 0.005;

            Administration adm = createAdministration(adminLibelle, typeFokontany);
            createLocalisation(
                    adminLibelle,
                    fkt.getLibelle() + ", Antananarivo 101",
                    latitude,
                    longitude,
                    "101",
                    adm);
            createService("Certificat de résidence et actes administratifs", adm);
            created++;
        }

        if (created > 0) {
            log.info("{} administrations de fokontany ajoutées (avec localisation).", created);
        }
    }

    private double[] centreArrondissement(String libelle) {
        return switch (libelle) {
            case "1er arrondissement" -> new double[]{-18.904, 47.524};
            case "2e arrondissement" -> new double[]{-18.892, 47.508};
            case "3e arrondissement" -> new double[]{-18.901, 47.537};
            case "4e arrondissement" -> new double[]{-18.917, 47.521};
            case "5e arrondissement" -> new double[]{-18.923, 47.539};
            default -> new double[]{-18.858, 47.519};
        };
    }

    private Commune createCommune(String libelle) {
        return communeRepository.save(Commune.builder().libelle(libelle).build());
    }

    private Arrondissement createArrondissement(String libelle, Commune commune) {
        return arrondissementRepository.save(Arrondissement.builder().libelle(libelle).commune(commune).build());
    }

    private Fokontany createFokontany(String libelle, Arrondissement arrondissement, String communeRurale) {
        return fokontanyRepository.save(Fokontany.builder().libelle(libelle).arrondissement(arrondissement).communeRurale(communeRurale).build());
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