package app.finplan.controller;

import app.finplan.dto.category.*;
import app.finplan.exception.ResourceException;
import app.finplan.handler.ApiResponse;
import app.finplan.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService catService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<List<CategoryDTO>>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int size
    ) {
        List<CategoryDTO> list = catService.list(page-1, size);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<CategoryDTO>> create(@RequestBody CategoryCreateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(catService.create(dto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<CategoryDTO>> update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(catService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(catService.delete(id)));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void clear() {
        try {
            catService.clear();
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant clear categories");
        }
    }
}
