package tn.esprit.autoloc.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.web.dto.TopClientDTO;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByClient_IdClient(Long idClient);

    List<Reservation> findByVehicule_IdVehiculeAndStatutNot(Long vehiculeId, StatutReservation statut);

    List<Reservation> findByStatut(StatutReservation statut);

    List<Reservation> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    List<Reservation> findByDateFinAfter(LocalDate date);

    List<Reservation> findByStatutAndVehicule_Agence_IdAgence(StatutReservation statut, Long agenceId);

    boolean existsByVehicule_IdVehiculeAndStatut(Long vehiculeId, StatutReservation statut);

    @Query("""
            SELECT new tn.esprit.autoloc.web.dto.TopClientDTO(c.idClient, c.nom, c.prenom, COUNT(r))
            FROM Reservation r JOIN r.client c
            GROUP BY c.idClient, c.nom, c.prenom
            ORDER BY COUNT(r) DESC
            """)
    List<TopClientDTO> topClients(Pageable pageable);
}