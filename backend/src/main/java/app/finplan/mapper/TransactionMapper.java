package app.finplan.mapper;

import app.finplan.dto.TransactionCreateDTO;
import app.finplan.dto.TransactionDTO;
import app.finplan.dto.TransactionUpdateDTO;
import app.finplan.model.Transaction;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class TransactionMapper {
    public abstract Transaction map(TransactionCreateDTO dto);
    public abstract TransactionDTO map(Transaction model);
}
