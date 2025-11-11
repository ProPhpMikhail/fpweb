package app.finplan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    @NotBlank
    String name;
    @NotBlank
    String amount;
}
