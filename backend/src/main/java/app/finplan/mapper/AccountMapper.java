package app.finplan.mapper;

import app.finplan.dto.account.AccountCreateDTO;
import app.finplan.dto.account.AccountDTO;
import app.finplan.dto.account.AccountUpdateDTO;
import app.finplan.dto.transaction.TransactionUpdateDTO;
import app.finplan.model.Account;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AccountMapper {
    public abstract Account map(AccountCreateDTO dto);

    @Mapping(target = "userId", source = "model.user.id")
    public abstract AccountDTO map(Account model);
    public abstract void update(AccountUpdateDTO dto, @MappingTarget Account model);
    public abstract void create(AccountCreateDTO dto, @MappingTarget Account model);
}
