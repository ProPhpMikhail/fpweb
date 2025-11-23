package app.finplan.controller;

import app.finplan.dto.transaction.TransactionCreateDTO;
import app.finplan.dto.transaction.TransactionDTO;
import app.finplan.dto.transaction.TransactionUpdateDTO;
import app.finplan.handler.ApiResponse;
import app.finplan.mapper.TransactionMapper;
import app.finplan.repositories.TransactionRepository;
import app.finplan.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<Page<TransactionDTO>>> list(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.ok(txService.list(page, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<TransactionDTO>> create(@RequestBody TransactionCreateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(txService.create(dto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<TransactionDTO>> update(@PathVariable Long id, @Valid @RequestBody TransactionUpdateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(txService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(txService.delete(id)));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void clear() {
        txService.clear();
    }
}
