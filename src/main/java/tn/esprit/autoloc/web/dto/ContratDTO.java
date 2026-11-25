package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratDTO {

    private Long idContrat;
    private LocalDate dateSignature;

    @NotNull(message = "Le montant total est obligatoire")
    @PositiveOrZero(message = "Le montant total ne peut pas être négatif")
    private BigDecimal montantTotal;

    private boolean valide;
    private Long reservationId;
}