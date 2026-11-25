package tn.esprit.autoloc.service.strategy;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

@Component
public class TarifStandardStrategy implements ITarificationStrategy {

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        long jours = Math.max(1, ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin()));
        return vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(jours));
    }

    @Override
    public String getCode() {
        return "STANDARD";
    }
}