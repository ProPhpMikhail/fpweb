package app.finplan.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    @NotBlank
    String name;
    Integer sort;
    @NotNull
    Long userId;
}
