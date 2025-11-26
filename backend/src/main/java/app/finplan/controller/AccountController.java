package app.finplan.controller;

import app.finplan.dto.account.AccountCreateDTO;
import app.finplan.dto.account.AccountDTO;
import app.finplan.dto.account.AccountUpdateDTO;
import app.finplan.exception.NotFoundException;
import app.finplan.exception.ResourceException;
import app.finplan.handler.ApiResponse;
import app.finplan.model.User;
import app.finplan.repositories.UserRepository;
import app.finplan.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<Page<AccountDTO>>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int size,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong access: " + userEmail)
        );
        Page<AccountDTO> list = accService.list(user.getId(), page-1, size);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<AccountDTO>> create(
            @RequestBody AccountCreateDTO dto,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong access: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(accService.create(user.getId(), dto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<AccountDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody AccountUpdateDTO dto,
            Principal principal
    ) {
        String userEmail = principal.getName();
        User user = userRepo.findByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("Wrong access: " + userEmail)
        );
        return ResponseEntity.ok(ApiResponse.ok(accService.update(id, user.getId(), dto)));
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
        return ResponseEntity.ok(ApiResponse.ok(accService.delete(user.getId(), id)));
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
