package tn.esprit.autoloc.service.factory;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;

/**
 * Sélectionne dynamiquement la stratégie de tarification adaptée à une réservation
 * (durée du séjour). Toutes les implémentations Spring de ITarificationStrategy
 * sont injectées automatiquement (Map code -> stratégie).
 */
@Component
public class TarificationStrategyFactory {

    private static final long SEUIL_LONGUE_DUREE = 7;
    private static final long SEUIL_COURT_SEJOUR = 3;

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(List<ITarificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ITarificationStrategy::getCode, Function.identity()));
    }

    public ITarificationStrategy resoudre(Reservation reservation) {
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        if (jours >= SEUIL_LONGUE_DUREE) {
            return strategies.get("LONGUE_DUREE");
        }
        if (jours <= SEUIL_COURT_SEJOUR && estWeekend(reservation)) {
            return strategies.get("WEEKEND");
        }
        return strategies.get("STANDARD");
    }

    private boolean estWeekend(Reservation reservation) {
        DayOfWeek jour = reservation.getDateDebut().getDayOfWeek();
        return jour == DayOfWeek.FRIDAY || jour == DayOfWeek.SATURDAY;
    }
}