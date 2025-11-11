package app.finplan.repositories;

import app.finplan.dto.TransactionCreateDTO;
import app.finplan.dto.TransactionDTO;
import app.finplan.dto.TransactionUpdateDTO;
import app.finplan.mapper.TransactionMapper;
import app.finplan.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {
    List<Transaction> transactions = new ArrayList<>();

    @Autowired
    TransactionMapper transactionMapper;

    public TransactionDTO add(TransactionCreateDTO transactionCreateDTO) {
        Transaction transaction = transactionMapper.map(transactionCreateDTO);
        transaction.setId(System.currentTimeMillis() / 1000);
        transactions.add(transaction);
        return transactionMapper.map(transaction);
    }

    public List<Transaction> all() {
        return transactions;
    }

    public TransactionDTO update (Long id, TransactionUpdateDTO transactionUpdateDTO) {
        Transaction transaction = transactions.stream()
                .filter(obj -> obj.getId().equals(id))
                .findFirst().orElseThrow();
        transaction.setName(transactionUpdateDTO.getName());
        transaction.setAmount(transactionUpdateDTO.getAmount());
        return transactionMapper.map(transaction);
    }

    public Long delete (Long id) {
        transactions.removeIf(obj -> obj.getId().equals(id));
        return id;
    }

    public void clear () {
        transactions.clear();
    }
}
