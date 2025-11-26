package app.finplan.repositories;

import app.finplan.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Page<Account> findByUserId(Long userId, Pageable pageable);

    Optional<Account> findByIdAndUserId(Long id, Long userId);
}
