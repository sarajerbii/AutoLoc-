package tn.esprit.autoloc.service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.TopClientDTO;

public interface IReservationService {
    ReservationResponseDTO create(ReservationRequestDTO dto);
    ReservationResponseDTO findById(Long id);
    List<ReservationResponseDTO> findAll();
    ReservationResponseDTO update(Long id, ReservationRequestDTO dto);
    void deleteById(Long id);

    Reservation creerReservation(Client client);
    Reservation annulerReservation(Reservation reservation);

    List<ReservationResponseDTO> findByStatut(StatutReservation statut);

    List<TopClientDTO> topClients(Pageable pageable);
}