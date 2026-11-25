package tn.esprit.autoloc.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

public interface IVehiculeService {
    VehiculeDTO create(VehiculeDTO dto);
    VehiculeDTO findById(Long id);
    List<VehiculeDTO> findAll();
    VehiculeDTO update(Long id, VehiculeDTO dto);
    void deleteById(Long id);
    void affecterVehiculeAAgence(Long vehiculeId, Long agenceId);
    void desaffecterVehiculeDeAgence(Long vehiculeId);
    List<VehiculeDTO> findByAgenceVille(String ville);

    Page<VehiculeDTO> findByStatut(StatutVehicule statut, Pageable pageable);

    List<VehiculeDTO> rechercherDisponibles(String ville, CategorieVehicule categorie,
                                            LocalDate dateDebut, LocalDate dateFin);
}