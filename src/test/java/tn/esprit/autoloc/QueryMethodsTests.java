package tn.esprit.autoloc;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@SpringBootTest
@Transactional
class QueryMethodsTests {

    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IClientRepository clientRepository;
    @Autowired private IReservationRepository reservationRepository;
    @Autowired private IContratRepository contratRepository;

    private Agence agence;
    private Client client;

    @BeforeEach
    void init() {
        Agence a = new Agence();
        a.setNom("Agence Query");
        a.setVille("VilleTest");
        agence = agenceRepository.save(a);

        Vehicule v = new Vehicule();
        v.setImmatriculation("999 TUN 1");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setAgence(agence);
        v = vehiculeRepository.save(v);

        Client c = new Client();
        c.setNom("Query");
        c.setPrenom("Test");
        c.setEmail("query.test@mail.com");
        c.setTelephone("20000000");
        c.setNumPermis("QRY001");
        c.setDateInscription(LocalDate.now());
        client = clientRepository.save(c);

        Reservation r = new Reservation();
        r.setClient(client);
        r.setVehicule(v);
        r.setDateDebut(LocalDate.of(2026, 12, 1));
        r.setDateFin(LocalDate.of(2026, 12, 5));
        r.setStatut(StatutReservation.CONFIRMEE);
        r = reservationRepository.save(r);

        Contrat ct = new Contrat();
        ct.setReservation(r);
        ct.setDateSignature(LocalDate.now());
        ct.setMontantTotal(new BigDecimal("450.00"));
        ct.setValide(true);
        contratRepository.save(ct);
    }

    @Test
    void testMethodesDerivees() {
        assertEquals(1, vehiculeRepository.findByAgenceVille("VilleTest").size());
        assertTrue(vehiculeRepository.findByAgenceVille("VilleInconnue").isEmpty());
        assertTrue(vehiculeRepository.existsByIdVehiculeAndStatut(
                vehiculeRepository.findByAgenceVille("VilleTest").get(0).getIdVehicule(),
                StatutVehicule.DISPONIBLE));
        assertTrue(clientRepository.findByEmail("query.test@mail.com").isPresent());
        assertTrue(clientRepository.findByEmail("absent@mail.com").isEmpty());
        assertEquals(1, reservationRepository.findByClient_IdClient(client.getIdClient()).size());
    }

    @Test
    void testVehiculesDisponibles() {
        var cat = CategorieVehicule.CITADINE;
        // période qui chevauche : exclu
        assertTrue(vehiculeRepository.rechercherVehiculesDisponibles("VilleTest", cat,
                LocalDate.of(2026, 12, 3), LocalDate.of(2026, 12, 8)).isEmpty());
        // période qui touche la borne : disponible
        assertEquals(1, vehiculeRepository.rechercherVehiculesDisponibles("VilleTest", cat,
                LocalDate.of(2026, 12, 5), LocalDate.of(2026, 12, 8)).size());
        // période sans rapport : disponible
        assertEquals(1, vehiculeRepository.rechercherVehiculesDisponibles("VilleTest", cat,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12)).size());
    }

    @Test
    void testAgregations() {
        assertTrue(reservationRepository.topClients(PageRequest.of(0, 100)).stream()
                .anyMatch(t -> t.clientId().equals(client.getIdClient()) && t.nombreReservations() == 1));

        var ca = contratRepository.chiffreAffaireParAgence().stream()
                .filter(d -> d.agenceId().equals(agence.getIdAgence())).findFirst();
        assertTrue(ca.isPresent());
        assertEquals(0, new BigDecimal("450.00").compareTo(ca.get().chiffreAffaires()));
    }

    @Test
    void testContratsAEcheanceProche() {
        assertFalse(contratRepository.trouverContratsAEcheanceProche(
                LocalDate.of(2026, 11, 30), LocalDate.of(2026, 12, 10)).isEmpty());
        assertTrue(contratRepository.trouverContratsAEcheanceProche(
                LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 10)).isEmpty());
    }
}