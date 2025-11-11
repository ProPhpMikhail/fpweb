package app.finplan.mapper;

import app.finplan.dto.TransactionCreateDTO;
import app.finplan.dto.TransactionDTO;
import app.finplan.model.Transaction;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-24T01:09:40+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class TransactionMapperImpl extends TransactionMapper {

    @Override
    public Transaction map(TransactionCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        String name = null;
        Double amount = null;

        name = dto.getName();
        amount = dto.getAmount();

        Long id = null;

        Transaction transaction = new Transaction( id, name, amount );

        return transaction;
    }

    @Override
    public TransactionDTO map(Transaction model) {
        if ( model == null ) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setId( model.getId() );
        transactionDTO.setName( model.getName() );
        if ( model.getAmount() != null ) {
            transactionDTO.setAmount( String.valueOf( model.getAmount() ) );
        }

        return transactionDTO;
    }
}
