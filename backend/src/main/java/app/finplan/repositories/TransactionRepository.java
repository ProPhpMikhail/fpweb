package app.finplan.repositories;

import app.finplan.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByCategoryId(Long categoryId);
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
}