package app.finplan.mapper;

import app.finplan.dto.category.CategoryCreateDTO;
import app.finplan.dto.category.CategoryDTO;
import app.finplan.dto.category.CategoryUpdateDTO;
import app.finplan.model.Category;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CategoryMapper {
    public abstract Category map(CategoryCreateDTO dto);

    @Mapping(target = "userId", source = "model.user.id")
    public abstract CategoryDTO map(Category model);
    public abstract void update(CategoryUpdateDTO dto, @MappingTarget Category model);
    public abstract void create(CategoryCreateDTO dto, @MappingTarget Category model);
}
