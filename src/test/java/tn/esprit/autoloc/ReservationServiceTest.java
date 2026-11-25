package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.service.IReservationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    private Client creerClient() {
        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Sami");
        client.setEmail("sami.benali@mail.com");
        client.setTelephone("22111222");
        client.setNumPermis("P123456");
        client.setDateInscription(LocalDate.now());
        return client;
    }

    @Test
    void testCreerReservationNotifieLesObservateurs() {
        Client client = creerClient();

        Reservation reservation = reservationService.creerReservation(client);

        assertNotNull(reservation);
        assertSame(client, reservation.getClient());
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
        // Console attendue :
        // [EMAIL] Confirmation envoyée au client sami.benali@mail.com ...
        // [SMS] Confirmation envoyée au 22111222 ...
    }

    @Test
    void testAnnulerReservationNotifieLesObservateurs() {
        Reservation reservation = reservationService.creerReservation(creerClient());

        Reservation annulee = reservationService.annulerReservation(reservation);

        assertEquals(StatutReservation.ANNULEE, annulee.getStatut());
        // Console attendue :
        // [EMAIL] Annulation notifiée au client sami.benali@mail.com ...
        // [SMS] Annulation notifiée au 22111222 ...
    }
}