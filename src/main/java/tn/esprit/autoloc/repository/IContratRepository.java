package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.domain.Contrat;
import org.springframework.data.jpa.repository.JpaRepository ;
import tn.esprit.autoloc.web.dto.ChiffreAffaireAgenceDTO;

import java.time.LocalDate;
import java.util.List;

public interface IContratRepository extends JpaRepository<Contrat, Long> {
    @Query("""
        SELECT new tn.esprit.autoloc.web.dto.ChiffreAffaireAgenceDTO(a.idAgence, a.nom, SUM(c.montantTotal))
        FROM Contrat c JOIN c.reservation r JOIN r.vehicule v JOIN v.agence a
        WHERE c.valide = true
        GROUP BY a.idAgence, a.nom
        ORDER BY SUM(c.montantTotal) DESC
        """)
    List<ChiffreAffaireAgenceDTO> chiffreAffaireParAgence();

    @Query("""
        SELECT c FROM Contrat c JOIN c.reservation r
        WHERE c.valide = true
          AND r.dateFin BETWEEN :aujourdhui AND :dateLimite
        ORDER BY r.dateFin
        """)
    List<Contrat> trouverContratsAEcheanceProche(
            @Param("aujourdhui") LocalDate aujourdhui,
            @Param("dateLimite") LocalDate dateLimite);
}