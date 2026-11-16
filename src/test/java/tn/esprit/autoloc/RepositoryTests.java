package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    // Les tests des étapes 3 à 6 vont ici

    @Test
    void testAgenceRepository() {
        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        // save()
        Agence saved = agenceRepository.save(agence);
        System.out.println("Agence enregistrée : id=" + saved.getIdAgence() + ", nom=" + saved.getNom());
        assertNotNull(saved.getIdAgence());

        // findAll()
        List<Agence> agences = agenceRepository.findAll();
        System.out.println("Toutes les agences : " + agences.size());
        assertFalse(agences.isEmpty());

        // findById()
        Optional<Agence> trouvee = agenceRepository.findById(saved.getIdAgence());
        assertTrue(trouvee.isPresent());
        System.out.println("Agence trouvée : " + trouvee.get().getNom());

        // existsById()
        assertTrue(agenceRepository.existsById(saved.getIdAgence()));

        // count()
        System.out.println("Nombre d'agences : " + agenceRepository.count());

        // deleteById()
        agenceRepository.deleteById(saved.getIdAgence());
        assertFalse(agenceRepository.existsById(saved.getIdAgence()));
        System.out.println("Agence supprimée, existsById = " + agenceRepository.existsById(saved.getIdAgence()));
    }

    @Test
    void testClientRepository() {
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

        // save() x2
        Client s1 = clientRepository.save(c1);
        Client s2 = clientRepository.save(c2);
        assertNotNull(s1.getIdClient());
        assertNotNull(s2.getIdClient());
        assertNotEquals(s1.getIdClient(), s2.getIdClient());

        // findAll()
        List<Client> clients = clientRepository.findAll();
        clients.forEach(c -> System.out.println("Client : " + c.getIdClient() + " - " + c.getNom() + " " + c.getPrenom()));

        // findById()
        Optional<Client> trouve = clientRepository.findById(s1.getIdClient());
        assertTrue(trouve.isPresent());
        assertEquals("Ben Ali", trouve.get().getNom());

        // existsById() : existant puis inexistant
        assertTrue(clientRepository.existsById(s1.getIdClient()));
        assertFalse(clientRepository.existsById(999999L));

        // count()
        System.out.println("Nombre de clients : " + clientRepository.count());
        assertTrue(clientRepository.count() >= 2);
    }

    @Test
    void testVehiculeRepository() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        // save()
        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());

        // findAll() et count()
        System.out.println("Véhicules : " + vehiculeRepository.findAll().size());
        System.out.println("Nombre de véhicules : " + vehiculeRepository.count());

        // modification puis save()
        saved.setStatut(StatutVehicule.LOUE);
        Vehicule modifie = vehiculeRepository.save(saved);
        assertEquals(StatutVehicule.LOUE, modifie.getStatut());
        System.out.println("Statut après modification : " + modifie.getStatut());

        // deleteById()
        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
    }

    @Autowired
    private IEquipementRepository equipementRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    @Autowired
    private IContratRepository contratRepository;

    @Test
    void testEquipementRepository() {
        Equipement e = new Equipement();
        e.setLibelle("GPS");

        Equipement saved = equipementRepository.save(e);
        assertNotNull(saved.getIdEquipement());
        System.out.println("Equipements : " + equipementRepository.findAll().size());

        assertTrue(equipementRepository.existsById(saved.getIdEquipement()));
        equipementRepository.deleteById(saved.getIdEquipement());
        assertFalse(equipementRepository.existsById(saved.getIdEquipement()));
    }

    @Test
    void testEmployeRepository() {
        Employe emp = new Employe();
        emp.setNom("Gharbi");
        emp.setPrenom("Yassine");
        emp.setRole(RoleEmploye.AGENT);

        Employe saved = employeRepository.save(emp);
        assertNotNull(saved.getIdEmploye());
        assertEquals(RoleEmploye.AGENT, employeRepository.findById(saved.getIdEmploye()).orElseThrow().getRole());

        assertTrue(employeRepository.existsById(saved.getIdEmploye()));
        employeRepository.deleteById(saved.getIdEmploye());
        assertFalse(employeRepository.existsById(saved.getIdEmploye()));
    }

    @Test
    void testContratRepository() {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("450.00"));
        contrat.setValide(true);

        Contrat saved = contratRepository.save(contrat);
        assertNotNull(saved.getIdContrat());
        System.out.println("Contrats : " + contratRepository.findAll().size());

        assertTrue(contratRepository.existsById(saved.getIdContrat()));
        contratRepository.deleteById(saved.getIdContrat());
        assertFalse(contratRepository.existsById(saved.getIdContrat()));
    }
}