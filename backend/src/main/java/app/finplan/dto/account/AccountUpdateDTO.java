package app.finplan.dto.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountUpdateDTO {
    String name;
    Integer sort;
    @NotNull
    BigDecimal balance;
}
