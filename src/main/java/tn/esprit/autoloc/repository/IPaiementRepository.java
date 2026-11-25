package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository ;
import tn.esprit.autoloc.domain.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByContrat_IdContrat(Long contratId);

    List<Paiement> findByDatePaiementBetween(LocalDate debut, LocalDate fin);
}
