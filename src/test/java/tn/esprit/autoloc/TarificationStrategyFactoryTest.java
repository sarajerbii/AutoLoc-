package tn.esprit.autoloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;
import tn.esprit.autoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.autoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.autoloc.service.strategy.TarifWeekendStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;
    private TarifStandardStrategy standard;
    private TarifWeekendStrategy weekend;
    private TarifLongueDureeStrategy longueDuree;

    @BeforeEach
    void setUp() {
        standard = new TarifStandardStrategy();
        weekend = new TarifWeekendStrategy();
        longueDuree = new TarifLongueDureeStrategy();
        factory = new TarificationStrategyFactory(List.of(standard, weekend, longueDuree));
    }

    private Reservation reservation(LocalDate debut, LocalDate fin) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(debut);
        reservation.setDateFin(fin);
        return reservation;
    }

    @Test
    void doitRetournerLongueDureePour7JoursOuPlus() {
        Reservation reservation = reservation(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 8));

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat).isInstanceOf(TarifLongueDureeStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerLongueDureePourExactement7Jours() {
        Reservation reservation = reservation(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 8));

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerWeekendPourCourtSejourVendredi() {
        // 2024-03-08 est un vendredi
        Reservation reservation = reservation(LocalDate.of(2024, 3, 8), LocalDate.of(2024, 3, 10));

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat).isInstanceOf(TarifWeekendStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("WEEKEND");
    }

    @Test
    void doitRetournerStandardPourMoyenneDuree() {
        // 2024-03-04 est un lundi, séjour de 4 jours
        Reservation reservation = reservation(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 3, 8));

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat.getCode()).isEqualTo("STANDARD");
    }
}