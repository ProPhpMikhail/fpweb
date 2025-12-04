package app.finplan.service;

import app.finplan.dto.transaction.*;
import app.finplan.exception.*;
import app.finplan.mapper.TransactionMapper;
import app.finplan.model.Account;
import app.finplan.model.Category;
import app.finplan.model.Transaction;
import app.finplan.model.User;
import app.finplan.repositories.AccountRepository;
import app.finplan.repositories.CategoryRepository;
import app.finplan.repositories.TransactionRepository;
import app.finplan.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository txRepo;
    private final UserRepository userRepo;
    private final AccountRepository accRepo;
    private final CategoryRepository catRepo;
    private final TransactionMapper txMapper;

    public Page<TransactionDTO> list(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Transaction> txPage = txRepo.findAll(pageable);

        return txPage.map(txMapper::map);
    }

    public Page<TransactionDTO> list(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Transaction> txPage = txRepo.findByUserId(userId, pageable);

        return txPage.map(txMapper::map);
    }

    @Transactional
    public TransactionDTO create(Long userId, TransactionCreateDTO dto) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new BusinessException(UserException.NOT_FOUND)
        );
        Account acc = accRepo.findByIdAndUserId(dto.getAccountId(), userId)
                .orElseThrow(() -> new BusinessException(AccountException.NOT_FOUND));

        BigDecimal newBalance = acc.getBalance().add(dto.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(AccountException.LESS_ZERO);
        }

        Transaction tx = new Transaction();
        txMapper.create(dto, tx);
        tx.setAccount(acc);
        tx.setUser(user);
        if (dto.getCategoryId() != null) {
            Optional<Category> cat = catRepo.findByIdAndUserId(dto.getCategoryId(), userId);
            cat.ifPresent(tx::setCategory);
        }
        tx = txRepo.save(tx);
        acc.setBalance(newBalance);
        accRepo.save(acc);

        return txMapper.map(tx);
    }

    @Transactional
    public TransactionDTO update(Long id, Long userId, TransactionUpdateDTO dto) {
        Transaction tx = txRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(TransactionException.NOT_FOUND));
        if (dto.getCategoryId() != null) {
            Optional<Category> cat = catRepo.findByIdAndUserId(dto.getCategoryId(), userId);
            cat.ifPresent(tx::setCategory);
        }

        Account acc = tx.getAccount();
        BigDecimal balanceBefore = getBalanceBeforeTransaction(acc, tx);
        System.out.println(balanceBefore);
        if (balanceBefore.add(dto.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(AccountException.LESS_ZERO);
        }
        acc.setBalance(acc.getBalance().subtract(tx.getAmount()).add(dto.getAmount()));

        txMapper.update(dto, tx);
        
        txRepo.save(tx);
        accRepo.save(acc);

        return txMapper.map(tx);
    }

    private BigDecimal getBalanceBeforeTransaction(Account account, Transaction transaction) {
        BigDecimal balanceBefore = account.getBalance();
        List<Transaction> afterTxList = txRepo.findAfter(account.getId(), transaction.getCreatedAt(), transaction.getId());
        for (Transaction trx : afterTxList) {
            balanceBefore = balanceBefore.subtract(trx.getAmount());
        }
        return balanceBefore.subtract(transaction.getAmount());
    }

    @Transactional
    public Long delete(Long userId, Long id) {
        Transaction tx = txRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(TransactionException.NOT_FOUND));
        Account acc = tx.getAccount();

        acc.setBalance(acc.getBalance().subtract(tx.getAmount()));
        accRepo.save(acc);

        txRepo.delete(tx);
        return id;
    }

    @Transactional
    public void clear() {
        try {
            txRepo.deleteAll();
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant clear transactions");
        }
    }
}
