package app.finplan.controller;

import app.finplan.dto.transaction.TransactionCreateDTO;
import app.finplan.dto.transaction.TransactionDTO;
import app.finplan.dto.transaction.TransactionFilter;
import app.finplan.dto.transaction.TransactionUpdateDTO;
import app.finplan.exception.NotFoundException;
import app.finplan.handler.ApiResponse;
import app.finplan.mapper.TransactionMapper;
import app.finplan.model.Transaction;
import app.finplan.model.User;
import app.finplan.repositories.TransactionRepository;
import app.finplan.repositories.UserRepository;
import app.finplan.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        @RequestParam(required = false) Long accountId,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) BigDecimal amountFrom,
        @RequestParam(required = false) BigDecimal amountTo,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtFrom,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtTo,
        Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong user: " + userEmail)
        );
        TransactionFilter filter = new TransactionFilter(
                accountId,
                categoryId,
                name,
                type,
                amountFrom,
                amountTo,
                createdAtFrom,
                createdAtTo
        );
        HashMap<String, Object> meta = new HashMap<>();
        if (filter.type() != null && !filter.type().isBlank() && filter.accountId() != null) {
            meta.put("sum", txService.getSummary(user.getId(), filter));
        }
        Page<TransactionDTO> list = txService.list(user.getId(), filter, page-1, size);
        return ResponseEntity.ok(ApiResponse.ok(list, meta));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<TransactionDTO>> create(
            @Valid @RequestBody TransactionCreateDTO dto,
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
