package app.finplan.service;

import app.finplan.dto.user.AuthResponse;
import app.finplan.dto.user.ConfirmEmailRequest;
import app.finplan.dto.user.LoginRequest;
import app.finplan.dto.user.RegisterRequest;
import app.finplan.exception.AuthException;
import app.finplan.model.User;
import app.finplan.model.UserRole;
import app.finplan.repositories.UserRepository;
import app.finplan.security.CustomUserDetailsService;
import app.finplan.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final MailService mailService;

    public void register(RegisterRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.email());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRole() == UserRole.UNCONFIRMED) {
                throw new AuthException("Email not confirmed", AuthException.EMAIL_NOT_CONFIRMED);
            } else {
                throw new AuthException("Email already in use", AuthException.EMAIL_EXISTS);
            }
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.UNCONFIRMED);
        String code = generateCode();
        user.setCode(code);
        userRepository.save(user);

        mailService.sendConfirmationCode(user.getEmail(), code);
    }

    public void confirmEmail(ConfirmEmailRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("User not found", AuthException.USER_NOT_FOUND));

        if (user.getCode() == null || !user.getCode().equals(request.code())) {
            throw new AuthException("Invalid confirmation code", AuthException.INVALID_CONFIRMATION_CODE);
        }

        user.setCode(null);
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        if (user.getRole() == UserRole.UNCONFIRMED) {
            throw new AuthException("Email not confirmed", AuthException.EMAIL_NOT_CONFIRMED);
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.email());

        if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new AuthException("Invalid credentials", AuthException.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getEmail(), user.getRole());
    }

    public void resendCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found", AuthException.USER_NOT_FOUND));

        if (user.getRole() != UserRole.UNCONFIRMED) {
            throw new AuthException("Email already confirmed", AuthException.EMAIL_ALREADY_CONFIRMED);
        }

        String code = generateCode();
        user.setCode(code);
        userRepository.save(user);

        mailService.sendConfirmationCode(user.getEmail(), code);
    }

    private String generateCode() {
        int code = (int) (Math.random() * 90_00) + 10_00;
        return String.valueOf(code);
    }
}
