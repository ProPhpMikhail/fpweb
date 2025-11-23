package app.finplan.dto.category;

import jakarta.validation.constraints.NotBlank;
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
}
