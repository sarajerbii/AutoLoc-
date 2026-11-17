package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTests {

    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEquipementService equipementService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IContratService contratService;

    // ---------- Partie 2 : AgenceService ----------
    @Test
    void testAgenceService() {
        Agence agence = new Agence();
        agence.setNom("AutoLoc Sfax");
        agence.setVille("Sfax");
        agence.setAdresse("Route de Tunis");
        agence.setTelephone("74000000");

        // create
        Agence creee = agenceService.create(agence);
        System.out.println("Agence créée : id=" + creee.getIdAgence() + ", nom=" + creee.getNom());
        assertNotNull(creee.getIdAgence());

        // findAll
        List<Agence> agences = agenceService.findAll();
        System.out.println("Toutes les agences : " + agences.size());
        assertFalse(agences.isEmpty());

        // findById
        Agence trouvee = agenceService.findById(creee.getIdAgence());
        System.out.println("Agence trouvée : " + trouvee.getNom());
        assertEquals("AutoLoc Sfax", trouvee.getNom());

        // "count" : nombre d'agences
        System.out.println("Nombre d'agences : " + agenceService.findAll().size());

        // update
        trouvee.setVille("Sousse");
        Agence modifiee = agenceService.update(creee.getIdAgence(), trouvee);
        System.out.println("Ville après modification : " + modifiee.getVille());
        assertEquals("Sousse", modifiee.getVille());

        // deleteById puis vérification
        agenceService.deleteById(creee.getIdAgence());
        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(creee.getIdAgence()));
        System.out.println("Agence supprimée : elle n'existe plus");
    }

    // ---------- Partie 3 : ClientService ----------
    @Test
    void testClientService() {
        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.com");
        c1.setTelephone("22111222");
        c1.setNumPermis("P123456");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Amel");
        c2.setEmail("amel.trabelsi@mail.com");
        c2.setTelephone("98333444");
        c2.setNumPermis("P654321");
        c2.setDateInscription(LocalDate.now());

        // create x2
        Client s1 = clientService.create(c1);
        Client s2 = clientService.create(c2);
        assertNotNull(s1.getIdClient());
        assertNotNull(s2.getIdClient());
        assertNotEquals(s1.getIdClient(), s2.getIdClient());

        // findAll
        List<Client> clients = clientService.findAll();
        clients.forEach(c -> System.out.println("Client : " + c.getIdClient() + " - " + c.getNom() + " " + c.getPrenom()));

        // findById
        Client trouve = clientService.findById(s1.getIdClient());
        assertEquals("Ben Ali", trouve.getNom());

        // "count" : nombre de clients
        System.out.println("Nombre de clients : " + clientService.findAll().size());
        assertTrue(clientService.findAll().size() >= 2);

        // update
        trouve.setTelephone("55555555");
        Client modifie = clientService.update(s1.getIdClient(), trouve);
        assertEquals("55555555", modifie.getTelephone());

        // deleteById puis vérification
        clientService.deleteById(s1.getIdClient());
        clientService.deleteById(s2.getIdClient());
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(s1.getIdClient()));
        System.out.println("Clients supprimés");
    }

    // ---------- Partie 4 : VehiculeService ----------
    @Test
    void testVehiculeService() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        // create
        Vehicule cree = vehiculeService.create(v);
        assertNotNull(cree.getIdVehicule());
        System.out.println("Véhicule créé : id=" + cree.getIdVehicule());

        // findAll
        System.out.println("Véhicules : " + vehiculeService.findAll().size());

        // findById
        Vehicule trouve = vehiculeService.findById(cree.getIdVehicule());
        assertEquals("Renault", trouve.getMarque());

        // "count" : nombre de véhicules
        System.out.println("Nombre de véhicules : " + vehiculeService.findAll().size());

        // update
        trouve.setStatut(StatutVehicule.LOUE);
        Vehicule modifie = vehiculeService.update(cree.getIdVehicule(), trouve);
        assertEquals(StatutVehicule.LOUE, modifie.getStatut());

        // règle de gestion : tarif négatif refusé
        Vehicule invalide = new Vehicule();
        invalide.setTarifJournalier(new BigDecimal("-10"));
        assertThrows(IllegalArgumentException.class, () -> vehiculeService.create(invalide));

        // deleteById puis vérification
        vehiculeService.deleteById(cree.getIdVehicule());
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(cree.getIdVehicule()));
        System.out.println("Véhicule supprimé");
    }

    // ---------- Partie 5 : exercice autonome (3 services) ----------
    @Test
    void testEquipementService() {
        Equipement e = new Equipement();
        e.setLibelle("GPS");

        Equipement cree = equipementService.create(e);
        assertNotNull(cree.getIdEquipement());
        System.out.println("Equipements : " + equipementService.findAll().size());

        assertNotNull(equipementService.findById(cree.getIdEquipement()));

        equipementService.deleteById(cree.getIdEquipement());
        assertThrows(ResourceNotFoundException.class,
                () -> equipementService.findById(cree.getIdEquipement()));
    }

    @Test
    void testEmployeService() {
        Employe emp = new Employe();
        emp.setNom("Gharbi");
        emp.setPrenom("Yassine");
        emp.setRole(RoleEmploye.AGENT);

        Employe cree = employeService.create(emp);
        assertNotNull(cree.getIdEmploye());
        assertEquals(RoleEmploye.AGENT, employeService.findById(cree.getIdEmploye()).getRole());
        System.out.println("Employés : " + employeService.findAll().size());

        employeService.deleteById(cree.getIdEmploye());
        assertThrows(ResourceNotFoundException.class,
                () -> employeService.findById(cree.getIdEmploye()));
    }

    @Test
    void testContratService() {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("450.00"));
        contrat.setValide(true);

        Contrat cree = contratService.create(contrat);
        assertNotNull(cree.getIdContrat());
        System.out.println("Contrats : " + contratService.findAll().size());

        assertNotNull(contratService.findById(cree.getIdContrat()));

        // règle de gestion : montant négatif refusé
        Contrat invalide = new Contrat();
        invalide.setMontantTotal(new BigDecimal("-1"));
        assertThrows(IllegalArgumentException.class, () -> contratService.create(invalide));

        contratService.deleteById(cree.getIdContrat());
        assertThrows(ResourceNotFoundException.class,
                () -> contratService.findById(cree.getIdContrat()));
    }
}