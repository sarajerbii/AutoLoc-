package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.dto.ClientDTO;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Test
    void testAgenceService() {
        AgenceDTO creee = agenceService.create(AgenceDTO.builder()
                .nom("AutoLoc Sfax").ville("Sfax").adresse("Route de Tunis").telephone("74000000").build());
        assertNotNull(creee.getIdAgence());
        assertFalse(agenceService.findAll().isEmpty());
        assertEquals("AutoLoc Sfax", agenceService.findById(creee.getIdAgence()).getNom());

        creee.setVille("Sousse");
        assertEquals("Sousse", agenceService.update(creee.getIdAgence(), creee).getVille());

        agenceService.deleteById(creee.getIdAgence());
        assertThrows(ResourceNotFoundException.class, () -> agenceService.findById(creee.getIdAgence()));
    }

    @Test
    void testClientService() {
        ClientDTO c1 = clientService.create(ClientDTO.builder()
                .nom("Ben Ali").prenom("Sami").email("sami.benali@mail.com")
                .telephone("22111222").numPermis("P123456").dateInscription(LocalDate.now()).build());
        ClientDTO c2 = clientService.create(ClientDTO.builder()
                .nom("Trabelsi").prenom("Amel").email("amel.trabelsi@mail.com")
                .telephone("98333444").numPermis("P654321").dateInscription(LocalDate.now()).build());
        assertNotNull(c1.getIdClient());
        assertNotEquals(c1.getIdClient(), c2.getIdClient());
        assertTrue(clientService.findAll().size() >= 2);
        assertEquals("Ben Ali", clientService.findById(c1.getIdClient()).getNom());

        c1.setTelephone("55555555");
        assertEquals("55555555", clientService.update(c1.getIdClient(), c1).getTelephone());

        clientService.deleteById(c1.getIdClient());
        clientService.deleteById(c2.getIdClient());
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(c1.getIdClient()));
    }

    @Test
    void testVehiculeService() {
        AgenceDTO agence = agenceService.create(AgenceDTO.builder().nom("Agence test").ville("Tunis").build());

        VehiculeDTO cree = vehiculeService.create(VehiculeDTO.builder()
                .immatriculation("123 TUN 4567").marque("Renault").modele("Clio")
                .categorie(CategorieVehicule.CITADINE).tarifJournalier(new BigDecimal("80.00"))
                .agenceId(agence.getIdAgence()).build());
        assertNotNull(cree.getIdVehicule());
        assertEquals(agence.getIdAgence(), cree.getAgenceId());

        VehiculeDTO invalide = VehiculeDTO.builder()
                .immatriculation("X").marque("Y").modele("Z").categorie(CategorieVehicule.SUV)
                .tarifJournalier(new BigDecimal("-10")).agenceId(agence.getIdAgence()).build();
        assertThrows(IllegalArgumentException.class, () -> vehiculeService.create(invalide));

        vehiculeService.desaffecterVehiculeDeAgence(cree.getIdVehicule());
        assertNull(vehiculeService.findById(cree.getIdVehicule()).getAgenceId());
        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.desaffecterVehiculeDeAgence(cree.getIdVehicule()));

        vehiculeService.affecterVehiculeAAgence(cree.getIdVehicule(), agence.getIdAgence());
        assertEquals(agence.getIdAgence(), vehiculeService.findById(cree.getIdVehicule()).getAgenceId());

        vehiculeService.deleteById(cree.getIdVehicule());
        agenceService.deleteById(agence.getIdAgence());
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.findById(cree.getIdVehicule()));
    }

    @Test
    void testEquipementService() {
        Equipement e = new Equipement();
        e.setLibelle("GPS");

        Equipement cree = equipementService.create(e);
        assertNotNull(cree.getIdEquipement());
        assertNotNull(equipementService.findById(cree.getIdEquipement()));

        equipementService.deleteById(cree.getIdEquipement());
        assertThrows(ResourceNotFoundException.class, () -> equipementService.findById(cree.getIdEquipement()));
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

        employeService.deleteById(cree.getIdEmploye());
        assertThrows(ResourceNotFoundException.class, () -> employeService.findById(cree.getIdEmploye()));
    }

    @Test
    void testContratService() {
        ContratDTO cree = contratService.create(ContratDTO.builder()
                .dateSignature(LocalDate.now()).montantTotal(new BigDecimal("450.00")).valide(true).build());
        assertNotNull(cree.getIdContrat());
        assertNotNull(contratService.findById(cree.getIdContrat()));

        ContratDTO invalide = ContratDTO.builder().montantTotal(new BigDecimal("-1")).build();
        assertThrows(IllegalArgumentException.class, () -> contratService.create(invalide));

        contratService.deleteById(cree.getIdContrat());
        assertThrows(ResourceNotFoundException.class, () -> contratService.findById(cree.getIdContrat()));
    }
}