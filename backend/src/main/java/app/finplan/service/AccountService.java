package app.finplan.service;

import app.finplan.dto.account.AccountCreateDTO;
import app.finplan.dto.account.AccountDTO;
import app.finplan.dto.account.AccountUpdateDTO;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.mapper.AccountMapper;
import app.finplan.model.Account;
import app.finplan.model.Transaction;
import app.finplan.repositories.AccountRepository;
import app.finplan.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accRepo;
    private final TransactionRepository txRepo;

    private final AccountMapper accMapper;

    public AccountService(AccountRepository accRepo, TransactionRepository txRepo, AccountMapper accMapper) {
        this.accRepo = accRepo;
        this.txRepo = txRepo;
        this.accMapper = accMapper;
    }

    public List<AccountDTO> list(int page, int limit) {
        Sort sort = Sort.by(Sort.Order.asc("sort"));
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        return accRepo.findAll(pageRequest).stream().map(accMapper::map).toList();
    }

    @Transactional
    public AccountDTO create(AccountCreateDTO dto) {
        Account acc = new Account();
        accMapper.create(dto, acc);
        acc = accRepo.save(acc);
        return accMapper.map(acc);
    }

    @Transactional
    public AccountDTO update(Long id, AccountUpdateDTO dto) {
        Account acc = accRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
        accMapper.update(dto, acc);
        acc = accRepo.save(acc);

        accRepo.save(acc);

        return accMapper.map(acc);
    }

    @Transactional
    public Long delete(Long id) {
        Account acc = accRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
        List<Transaction> transactions = txRepo.findByAccountId(id);
        transactions.forEach(txRepo::delete);
        accRepo.delete(acc);
        return id;
    }

    @Transactional
    public void clear() {
        try {
            accRepo.deleteAll();
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant clear accounts");
        }
    }
}
