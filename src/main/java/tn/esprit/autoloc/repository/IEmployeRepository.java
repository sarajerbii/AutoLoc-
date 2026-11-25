package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository ;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.enums.RoleEmploye;

import java.util.List;

public interface IEmployeRepository extends JpaRepository<Employe, Long>  {
    List<Employe> findByAgenceNom(String nomAgence);

    List<Employe> findByAgence_IdAgenceAndRole(Long agenceId, RoleEmploye role);

    List<Employe> findByNomContaining(String nom);
}
