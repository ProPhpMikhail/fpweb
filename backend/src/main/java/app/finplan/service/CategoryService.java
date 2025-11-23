package app.finplan.service;

import app.finplan.dto.category.*;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.mapper.CategoryMapper;
import app.finplan.model.Category;
import app.finplan.model.Transaction;
import app.finplan.repositories.CategoryRepository;
import app.finplan.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository catRepo;
    private final TransactionRepository txRepo;
    private final CategoryMapper catMapper;

    public CategoryService(CategoryRepository catRepo, TransactionRepository txRepo, CategoryMapper catMapper) {
        this.catRepo = catRepo;
        this.txRepo = txRepo;
        this.catMapper = catMapper;
    }

    public List<CategoryDTO> list(int page, int limit) {
        Sort sort = Sort.by(Sort.Order.asc("sort"));
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        return catRepo.findAll(pageRequest).stream().map(catMapper::map).toList();
    }

    @Transactional
    public CategoryDTO create(CategoryCreateDTO dto) {
        Category cat = new Category();
        catMapper.create(dto, cat);
        cat = catRepo.save(cat);
        return catMapper.map(cat);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryUpdateDTO dto) {
        Category cat = catRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        catMapper.update(dto, cat);
        cat = catRepo.save(cat);

        return catMapper.map(cat);
    }

    @Transactional
    public Long delete(Long id) {
        Category cat = catRepo.findById(id)
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
