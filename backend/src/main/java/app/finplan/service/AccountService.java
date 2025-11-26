package app.finplan.service;

import app.finplan.dto.account.AccountCreateDTO;
import app.finplan.dto.account.AccountDTO;
import app.finplan.dto.account.AccountUpdateDTO;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.mapper.AccountMapper;
import app.finplan.model.Account;
import app.finplan.model.Transaction;
import app.finplan.model.User;
import app.finplan.repositories.AccountRepository;
import app.finplan.repositories.TransactionRepository;
import app.finplan.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accRepo;
    private final UserRepository userRepo;
    private final TransactionRepository txRepo;

    private final AccountMapper accMapper;

    public Page<AccountDTO> list(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("sort").ascending());
        return accRepo.findAll(pageable).map(accMapper::map);
    }

    public Page<AccountDTO> list(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("sort").ascending());
        return accRepo.findByUserId(userId, pageable).map(accMapper::map);
    }

    @Transactional
    public AccountDTO create(Long userId, AccountCreateDTO dto) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found: " + userId)
        );
        Account acc = new Account();
        accMapper.create(dto, acc);
        acc.setUser(user);
        acc = accRepo.save(acc);
        return accMapper.map(acc);
    }

    @Transactional
    public AccountDTO update(Long id, Long userId, AccountUpdateDTO dto) {
        Account acc = accRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
        accMapper.update(dto, acc);
        acc = accRepo.save(acc);

        accRepo.save(acc);

        return accMapper.map(acc);
    }

    @Transactional
    public Long delete(Long userId, Long id) {
        Account acc = accRepo.findByIdAndUserId(id, userId)
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
