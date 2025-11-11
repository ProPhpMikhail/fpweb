package app.finplan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionUpdateDTO {
    String name;
    Double amount;

    @Override
    public String toString() {
        return "TransactionCreateDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
