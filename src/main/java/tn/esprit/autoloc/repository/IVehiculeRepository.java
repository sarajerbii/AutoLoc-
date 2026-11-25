package tn.esprit.autoloc.repository;

//import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;

//import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
    List<Vehicule> findByAgenceVille(String ville);

    List<Vehicule> findByStatutAndAgence_IdAgence(StatutVehicule statut, Long agenceId);

    Page<Vehicule> findByStatut(StatutVehicule statut, Pageable pageable);

    List<Vehicule> findByAgence_VilleAndCategorie(String ville, CategorieVehicule categorie);

    boolean existsByIdVehiculeAndStatut(Long vehiculeId, StatutVehicule statut);

    long countByStatut(StatutVehicule statut);

    List<Vehicule> findByTarifJournalierLessThan(BigDecimal tarif);

    List<Vehicule> findByTarifJournalierGreaterThan(BigDecimal tarif);

    List<Vehicule> findAllByOrderByTarifJournalierAsc();
    @Query("""
        SELECT v FROM Vehicule v
        WHERE v.agence.ville = :ville
          AND v.categorie = :categorie
          AND v.statut = tn.esprit.autoloc.domain.enums.StatutVehicule.DISPONIBLE
          AND NOT EXISTS (
              SELECT r FROM Reservation r
              WHERE r.vehicule = v
                AND r.statut <> tn.esprit.autoloc.domain.enums.StatutReservation.ANNULEE
                AND r.dateDebut < :dateFin
                AND r.dateFin > :dateDebut)
        """)
    List<Vehicule> rechercherVehiculesDisponibles(
            @Param("ville") String ville,
            @Param("categorie") CategorieVehicule categorie,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    @Query("""
        SELECT v FROM Vehicule v
        WHERE v.statut <> tn.esprit.autoloc.domain.enums.StatutVehicule.DISPONIBLE
          AND EXISTS (
              SELECT r FROM Reservation r
              WHERE r.vehicule = v
                AND r.statut = tn.esprit.autoloc.domain.enums.StatutReservation.CONFIRMEE
                AND r.dateFin <= :aujourdhui)
        """)
    List<Vehicule> trouverVehiculesAReleaser(@Param("aujourdhui") LocalDate aujourdhui);
}