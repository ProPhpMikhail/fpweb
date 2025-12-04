package app.finplan.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountCreateDTO {
    @NotBlank
    String name;
    Integer sort = 1;
    @NotBlank
    String currency;
    @NotNull
    BigDecimal balance;
}
