package app.finplan.service;

import app.finplan.dto.category.*;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.mapper.CategoryMapper;
import app.finplan.model.Category;
import app.finplan.model.Transaction;
import app.finplan.model.User;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository catRepo;
    private final UserRepository userRepo;
    private final CategoryMapper catMapper;

    public Page<CategoryDTO> list(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("sort").ascending());
        return catRepo.findAll(pageable).map(catMapper::map);
    }

    public Page<CategoryDTO> list(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("sort").ascending());
        return catRepo.findByUserId(userId, pageable).map(catMapper::map);
    }

    @Transactional
    public CategoryDTO create(Long userId, CategoryCreateDTO dto) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found: " + userId)
        );
        Category cat = new Category();
        catMapper.create(dto, cat);
        cat.setUser(user);
        cat = catRepo.save(cat);
        return catMapper.map(cat);
    }

    @Transactional
    public CategoryDTO update(Long id, Long userId, CategoryUpdateDTO dto) {
        Category cat = catRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        catMapper.update(dto, cat);
        cat = catRepo.save(cat);

        return catMapper.map(cat);
    }

    @Transactional
    public Long delete(Long userId, Long id) {
        Category cat = catRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        catRepo.delete(cat);
        return id;
    }

    @Transactional
    public void clear() {
        try {
            catRepo.deleteAll();
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant clear categories");
        }
    }
}
