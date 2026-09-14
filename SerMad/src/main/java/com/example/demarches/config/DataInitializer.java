package com.example.demarches.config;

import com.example.demarches.model.*;
import com.example.demarches.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        if (userRepository.count() > 0) {
            log.info("Database already initialized, skipping...");
            return;
        }

        log.info("Initializing database with default data...");

        // 1. Profiles
        Profile citoyen = createProfile("Citoyen");
        Profile agent = createProfile("Agent");
        Profile admin = createProfile("Admin");

        // 2. Actions
        Action view = createAction("VIEW");
        Action create = createAction("CREATE");
        Action update = createAction("UPDATE");
        Action delete = createAction("DELETE");

        // 3. Entities
        Entite userEntity = createEntite("User");
        Entite demandeEntity = createEntite("Demande");
        Entite documentEntity = createEntite("Document");
        Entite administrationEntity = createEntite("Administration");
        Entite notificationEntity = createEntite("Notification");
        Entite validationEntity = createEntite("Validation");
        Entite procedureEntity = createEntite("Procedure");
        Entite serviceEntity = createEntite("Service");

        // 4. RBAC
        // Agent
        createPermission(agent, view, userEntity);
        createPermission(agent, view, demandeEntity);
        createPermission(agent, view, documentEntity);
        createPermission(agent, view, administrationEntity);
        createPermission(agent, view, notificationEntity);
        createPermission(agent, view, serviceEntity);
        createPermission(agent, create, demandeEntity);
        createPermission(agent, update, demandeEntity);

        // Admin
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

        // 5. Statuts de demande
        StatutDemande statutEnAttente = createStatut("En attente validation citoyen");
        StatutDemande statutValidee = createStatut("Validée par citoyen");
        StatutDemande statutEnCours = createStatut("En cours de traitement");
        StatutDemande statutTerminee = createStatut("Terminée");
        StatutDemande statutRejetee = createStatut("Rejetée");

        // 6. Types d'administration
        TypeAdm typeMairie = createTypeAdm("Mairie");
        TypeAdm typePrefecture = createTypeAdm("Préfecture");
        TypeAdm typeCaisse = createTypeAdm("Caisse Nationale de Sécurité Sociale");
        TypeAdm typeImpots = createTypeAdm("Direction Générale des Impôts");

        // 7. Administrations
        Administration mairieRabat = createAdministration("Mairie de Rabat", typeMairie);
        Administration prefRabat = createAdministration("Préfecture de Rabat-Salé-Kénitra", typePrefecture);
        Administration cnss = createAdministration("CNSS Agence Rabat", typeCaisse);
        Administration dgiRabat = createAdministration("DGI Agence Rabat", typeImpots);

        // 8. Localisations
        createLocalisation("Siège Mairie", "Avenue Bab El Had, Rabat", -6.8326, 34.0132, "10000", mairieRabat);
        createLocalisation("Siège Préfecture", "Avenue de France, Rabat", -6.8410, 34.0090, "10020", prefRabat);
        createLocalisation("Agence CNSS", "Avenue Ibn Batouta, Rabat", -6.8210, 33.9900, "10050", cnss);
        createLocalisation("Agence DGI", "Rue Oujda, Rabat", -6.8100, 33.9850, "10030", dgiRabat);

        // 9. Services
        Service etatCivil = createService("État Civil", mairieRabat);
        Service urbanisme = createService("Urbanisme", mairieRabat);
        Service securite = createService("Sécurité", prefRabat);
        Service alloc = createService("Allocations familiales", cnss);
        Service declaration = createService("Déclarations fiscales", dgiRabat);

        // 10. Catégories & Types de document
        Categorie catEtatCivil = createCategorie("État civil");
        Categorie catImmobilier = createCategorie("Immobilier");
        Categorie catFiscal = createCategorie("Fiscal");

        TypeDocument typeActeNaissance = createTypeDocument("Acte de naissance");
        TypeDocument typeCNIS = createTypeDocument("Carte nationale d'identité");
        TypeDocument typePasseport = createTypeDocument("Passeport");
        TypeDocument typePermis = createTypeDocument("Permis de conduire");
        TypeDocument typeQuittance = createTypeDocument("Quittance d'impôt");
        TypeDocument typeJusticDomicile = createTypeDocument("Justificatif de domicile");

        // 11. Documents
        Document docActeNaissance = createDocument("Acte de naissance", 0, catEtatCivil, mairieRabat, typeActeNaissance);
        Document docCNI = createDocument("Carte nationale d'identité", 18, catEtatCivil, prefRabat, typeCNIS);
        Document docPasseport = createDocument("Passeport biométrique", 18, catEtatCivil, prefRabat, typePasseport);
        Document docPermis = createDocument("Permis de conduire B", 18, catEtatCivil, prefRabat, typePermis);
        Document docQuittance = createDocument("Quittance d'impôt 2026", 0, catFiscal, dgiRabat, typeQuittance);
        Document docJusticDom = createDocument("Attestation de domicile", 0, catImmobilier, mairieRabat, typeJusticDomicile);

        // 12. Procédures
        ProcedureFille pf1 = createProcedureFille(30.0, 10.0);
        ProcedureFille pf2 = createProcedureFille(60.0, 50.0);
        ProcedureFille pf3 = createProcedureFille(15.0, 0.0);
        ProcedureFille pf4 = createProcedureFille(90.0, 200.0);
        ProcedureFille pf5 = createProcedureFille(7.0, 0.0);

        ProcedureMere pm1 = createProcedureMere(docActeNaissance, pf1);
        ProcedureMere pm2 = createProcedureMere(docCNI, pf2);
        ProcedureMere pm3 = createProcedureMere(docPasseport, pf2);
        ProcedureMere pm4 = createProcedureMere(docPermis, pf4);
        ProcedureMere pm5 = createProcedureMere(docQuittance, pf5);
        ProcedureMere pm6 = createProcedureMere(docJusticDom, pf3);

        // 13. Users
        User adminUser = createUser("Admin", "System", "admin@demarches.ma", "admin123", "0600000000", "AB123456");
        User agentUser = createUser("Agent", "Test", "agent@demarches.ma", "agent123", "0611111111", "CD789012");
        User citoyenUser = createUser("Citoyen", "Test", "citoyen@demarches.ma", "citoyen123", "0622222222", "EF345678");
        User citoyen2 = createUser("Dupont", "Marie", "marie@demarches.ma", "citoyen123", "0633333333", "GH901234");
        User citoyen3 = createUser("Alami", "Ahmed", "ahmed@demarches.ma", "citoyen123", "0644444444", "IJ567890");

        // 14. Assign profiles
        assignProfile(adminUser, admin);
        assignProfile(agentUser, agent);
        assignProfile(citoyenUser, citoyen);
        assignProfile(citoyen2, citoyen);
        assignProfile(citoyen3, citoyen);

        // 15. Sample demandes
        createDemande("Demande acte de naissance", "DEM-001", statutEnAttente, pm1, citoyenUser);
        createDemande("Demande CNI", "DEM-002", statutValidee, pm2, citoyenUser);
        createDemande("Demande passeport", "DEM-003", statutEnCours, pm3, citoyen2);
        createDemande("Demande permis de conduire", "DEM-004", statutEnAttente, pm4, citoyen3);
        createDemande("Déclaration impôt 2026", "DEM-005", statutTerminee, pm5, citoyenUser);

        log.info("=== DATABASE INITIALIZED SUCCESSFULLY ===");
        log.info("=== LOGIN CREDENTIALS ===");
        log.info("Admin:   admin@demarches.ma / admin123");
        log.info("Agent:   agent@demarches.ma / agent123");
        log.info("Citoyen: citoyen@demarches.ma / citoyen123");
        log.info("Citoyen: marie@demarches.ma / citoyen123");
        log.info("Citoyen: ahmed@demarches.ma / citoyen123");
    }

    private Profile createProfile(String nom) {
        return profileRepository.findByNom(nom)
                .orElseGet(() -> profileRepository.save(Profile.builder().nom(nom).build()));
    }

    private Action createAction(String libelle) {
        return actionRepository.save(Action.builder().libelle(libelle).build());
    }

    private Entite createEntite(String libelle) {
        return entiteRepository.save(Entite.builder().libelle(libelle).build());
    }

    private void createPermission(Profile profile, Action action, Entite entite) {
        asso2Repository.save(Asso2.builder()
                .profile(profile).action(action).entite(entite).build());
    }

    private StatutDemande createStatut(String libelle) {
        return statutDemandeRepository.findByLibelle(libelle)
                .orElseGet(() -> statutDemandeRepository.save(StatutDemande.builder().libelle(libelle).build()));
    }

    private TypeAdm createTypeAdm(String libelle) {
        return typeAdmRepository.findByLibelle(libelle)
                .orElseGet(() -> typeAdmRepository.save(TypeAdm.builder().libelle(libelle).build()));
    }

    private Administration createAdministration(String libelle, TypeAdm type) {
        return administrationRepository.save(Administration.builder().libelle(libelle).typeAdm(type).build());
    }

    private void createLocalisation(String libelle, String adresse, Double lon, Double lat, String cp, Administration adm) {
        localisationAdmRepository.save(LocalisationAdm.builder()
                .libelle(libelle).adresse(adresse).longitude(lon).latitude(lat).codePostal(cp).administration(adm).build());
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

    private ProcedureFille createProcedureFille(Double delai, Double cout) {
        return procedureFilleRepository.save(ProcedureFille.builder().delai(delai).cout(cout).build());
    }

    private ProcedureMere createProcedureMere(Document doc, ProcedureFille pf) {
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
}
