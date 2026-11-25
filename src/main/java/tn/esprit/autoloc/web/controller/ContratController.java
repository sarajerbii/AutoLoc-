package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.PaiementDTO;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister les contrats")
    @GetMapping
    public List<ContratDTO> findAll() {
        return contratService.findAll();
    }

    @Operation(summary = "Consulter un contrat")
    @GetMapping("/{id}")
    public ContratDTO findById(@PathVariable Long id) {
        return contratService.findById(id);
    }

    @Operation(summary = "Créer un contrat")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDTO create(@Valid @RequestBody ContratDTO dto) {
        return contratService.create(dto);
    }

    @Operation(summary = "Modifier un contrat")
    @PutMapping("/{id}")
    public ContratDTO update(@PathVariable Long id, @Valid @RequestBody ContratDTO dto) {
        return contratService.update(id, dto);
    }

    @Operation(summary = "Supprimer un contrat et ses paiements")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        contratService.deleteById(id);
    }

    @Operation(summary = "Ajouter un paiement à un contrat")
    @PostMapping("/{contratId}/paiements")
    @ResponseStatus(HttpStatus.CREATED)
    public PaiementDTO ajouterPaiement(@PathVariable Long contratId, @Valid @RequestBody PaiementDTO dto) {
        return contratService.ajouterPaiement(contratId, dto);
    }

    @Operation(summary = "Contrats arrivant à échéance sous N jours")
    @GetMapping("/echeance-proche")
    public List<ContratDTO> echeanceProche(@RequestParam(defaultValue = "30") int jours) {
        return contratService.contratsAEcheanceProche(jours);
    }
}