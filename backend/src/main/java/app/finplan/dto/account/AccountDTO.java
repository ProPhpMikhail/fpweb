package app.finplan.dto.account;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountDTO {
    private Long id;
    @NotBlank
    String name;
    Integer sort;
    @NotBlank
    String currency;
    @NotNull
    BigDecimal balance;
}
