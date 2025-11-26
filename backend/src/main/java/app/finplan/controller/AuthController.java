package app.finplan.controller;

import app.finplan.dto.user.*;
import app.finplan.handler.ApiResponse;
import app.finplan.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/confirm")
    public void confirm(@RequestBody ConfirmEmailRequest request) {
        authService.confirmEmail(request);
    }

    @PostMapping("/resend")
    public void resend(@RequestBody ResendCodeRequest request) {
        authService.resendCode(request.email());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(request)));
    }
}
