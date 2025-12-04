package app.finplan.controller;

import app.finplan.dto.category.*;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.handler.ApiResponse;
import app.finplan.model.User;
import app.finplan.repositories.UserRepository;
import app.finplan.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService catService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<Page<CategoryDTO>>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int size,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong user: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(catService.list(user.getId(), page-1, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<CategoryDTO>> create(
            @Valid @RequestBody CategoryCreateDTO dto,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong access: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(catService.create(user.getId(), dto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<CategoryDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO dto,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong access: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(catService.update(id, user.getId(), dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Long>> delete(
            @PathVariable Long id,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong access: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(catService.delete(user.getId(), id)));
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
