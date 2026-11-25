package tn.esprit.autoloc.service.strategy;

import java.math.BigDecimal;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

public interface ITarificationStrategy {

    BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);

    String getCode(); // utilisé pour sélectionner dynamiquement la stratégie
}