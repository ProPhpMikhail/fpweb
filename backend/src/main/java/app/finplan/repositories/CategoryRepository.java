package app.finplan.repositories;

import app.finplan.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByUserId(Long userId, Pageable pageable);

    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
