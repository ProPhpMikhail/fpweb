package app.finplan.controller;

import app.finplan.dto.transaction.TransactionCreateDTO;
import app.finplan.dto.transaction.TransactionDTO;
import app.finplan.dto.transaction.TransactionUpdateDTO;
import app.finplan.exception.NotFoundException;
import app.finplan.handler.ApiResponse;
import app.finplan.mapper.TransactionMapper;
import app.finplan.model.User;
import app.finplan.repositories.TransactionRepository;
import app.finplan.repositories.UserRepository;
import app.finplan.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService txService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<Page<TransactionDTO>>> list(
        @RequestParam(defaultValue="1") int page,
        @RequestParam(defaultValue="20") int size,
        Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong user: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(txService.list(user.getId(), page-1, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<TransactionDTO>> create(
            @RequestBody TransactionCreateDTO dto,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong user: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(txService.create(user.getId(), dto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<TransactionDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateDTO dto,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong user: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(txService.update(id, user.getId(), dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Long>> delete(
            @PathVariable Long id,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong user: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(txService.delete(user.getId(), id)));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void clear() {
        txService.clear();
    }
}
