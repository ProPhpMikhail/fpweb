package app.finplan.service;

import app.finplan.dto.transaction.*;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.mapper.TransactionMapper;
import app.finplan.model.Account;
import app.finplan.model.Category;
import app.finplan.model.Transaction;
import app.finplan.repositories.AccountRepository;
import app.finplan.repositories.CategoryRepository;
import app.finplan.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository txRepo;

    private final AccountRepository accRepo;
    private final CategoryRepository catRepo;
    private final TransactionMapper txMapper;

    public TransactionService(TransactionRepository txRepo, AccountRepository accRepo, CategoryRepository catRepo, TransactionMapper txMapper) {
        this.txRepo = txRepo;
        this.accRepo = accRepo;
        this.catRepo = catRepo;
        this.txMapper = txMapper;
    }

    public Page<TransactionDTO> list(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Transaction> txPage = txRepo.findAll(pageable);

        return txPage.map(txMapper::map);
    }

    @Transactional
    public TransactionDTO create(TransactionCreateDTO dto) {
        Account acc = accRepo.findById(dto.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found: " + dto.getAccountId()));
        Transaction tx = new Transaction();
        txMapper.create(dto, tx);
        tx.setAccount(acc);
        if (dto.getCategoryId() != null) {
            Category cat = catRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategoryId()));
            tx.setCategory(cat);
        }
        tx = txRepo.save(tx);

        acc.setBalance(acc.getBalance().add(dto.getAmount()));
        accRepo.save(acc);

        return txMapper.map(tx);
    }

    @Transactional
    public TransactionDTO update(Long id, TransactionUpdateDTO dto) {
        Transaction tx = txRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
        if (dto.getCategoryId() != null) {
            Category cat = catRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategoryId()));

            tx.setCategory(cat);
        }

        Account acc = tx.getAccount();
        acc.setBalance(acc.getBalance().subtract(tx.getAmount()).add(dto.getAmount()));

        txMapper.update(dto, tx);
        
        txRepo.save(tx);
        accRepo.save(acc);

        return txMapper.map(tx);
    }

    @Transactional
    public Long delete(Long id) {
        Transaction tx = txRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
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
