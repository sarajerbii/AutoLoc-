package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.PaiementDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;
    private final PaiementMapper paiementMapper;

    @Override
    public ContratDTO create(ContratDTO dto) {
        if (dto.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        verifierMontant(dto);
        Contrat contrat = contratMapper.toEntity(dto);
        contrat.setReservation(trouverReservation(dto.getReservationId()));
        return contratMapper.toDto(contratRepository.save(contrat));
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {
        return contratMapper.toDto(trouver(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {
        return contratRepository.findAll().stream().map(contratMapper::toDto).toList();
    }

    @Override
    public ContratDTO update(Long id, ContratDTO dto) {
        verifierMontant(dto);
        Contrat existant = trouver(id);
        existant.setDateSignature(dto.getDateSignature());
        existant.setMontantTotal(dto.getMontantTotal());
        existant.setValide(dto.isValide());
        if (dto.getReservationId() != null) {
            existant.setReservation(trouverReservation(dto.getReservationId()));
        }
        return contratMapper.toDto(contratRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    @Override
    public PaiementDTO ajouterPaiement(Long contratId, PaiementDTO dto) {
        if (dto.getIdPaiement() != null) {
            throw new IllegalArgumentException("Un nouveau paiement ne doit pas avoir d'identifiant");
        }
        if (!contratId.equals(dto.getContratId())) {
            throw new IllegalArgumentException("Le contrat du paiement ne correspond pas à l'URL");
        }
        Contrat contrat = trouver(contratId);
        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setContrat(contrat);
        contrat.getPaiements().add(paiement);
        contratRepository.flush();
        return paiementMapper.toDto(paiement);
    }

    private Contrat trouver(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    private Reservation trouverReservation(Long reservationId) {
        if (reservationId == null) {
            return null;
        }
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));
    }

    private void verifierMontant(ContratDTO dto) {
        BigDecimal montant = dto.getMontantTotal();
        if (montant != null && montant.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
    }
}