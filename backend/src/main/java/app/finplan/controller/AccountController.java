package app.finplan.controller;

import app.finplan.dto.account.AccountCreateDTO;
import app.finplan.dto.account.AccountDTO;
import app.finplan.dto.account.AccountUpdateDTO;
import app.finplan.exception.ResourceException;
import app.finplan.handler.ApiResponse;
import app.finplan.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<List<AccountDTO>>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int size
    ) {
        List<AccountDTO> list = accService.list(page, size);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<AccountDTO>> create(@RequestBody AccountCreateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(accService.create(dto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<AccountDTO>> update(@PathVariable Long id, @Valid @RequestBody AccountUpdateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(accService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(accService.delete(id)));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void clear() {
        try {
            accService.clear();
        } catch (RuntimeException exception) {
            throw new ResourceException("Cant clear accounts");
        }
    }
}
