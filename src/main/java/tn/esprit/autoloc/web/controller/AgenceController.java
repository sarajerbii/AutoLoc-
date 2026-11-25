package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences")
public class AgenceController {

    private final IAgenceService agenceService;

    @Operation(summary = "Lister les agences")
    @GetMapping
    public List<AgenceDTO> findAll() {
        return agenceService.findAll();
    }

    @Operation(summary = "Consulter une agence")
    @GetMapping("/{id}")
    public AgenceDTO findById(@PathVariable Long id) {
        return agenceService.findById(id);
    }

    @Operation(summary = "Créer une agence")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenceDTO create(@Valid @RequestBody AgenceDTO dto) {
        return agenceService.create(dto);
    }

    @Operation(summary = "Modifier une agence")
    @PutMapping("/{id}")
    public AgenceDTO update(@PathVariable Long id, @Valid @RequestBody AgenceDTO dto) {
        return agenceService.update(id, dto);
    }

    @Operation(summary = "Supprimer une agence")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        agenceService.deleteById(id);
    }
}