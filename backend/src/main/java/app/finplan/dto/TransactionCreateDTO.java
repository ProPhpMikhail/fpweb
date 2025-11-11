package app.finplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCreateDTO {
    @NotBlank
    String name;
    @NotBlank
    Double amount;

    @Override
    public String toString() {
        return "TransactionCreateDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
