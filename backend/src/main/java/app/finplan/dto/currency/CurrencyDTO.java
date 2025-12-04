package app.finplan.dto.currency;

import jakarta.validation.constraints.NotBlank;

public record CurrencyDTO(
        @NotBlank
        String name,
        @NotBlank
        String id
) {
}
