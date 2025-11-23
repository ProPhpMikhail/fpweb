package app.finplan.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountCreateDTO {
    String name;
    Integer sort;
    @NotBlank
    String currency;
    @NotNull
    BigDecimal balance;
}
