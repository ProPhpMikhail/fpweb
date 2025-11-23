package app.finplan.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDTO {
    @NotBlank
    String name;
    Integer sort;
}
