package app.finplan.handler;

import app.finplan.exception.AuthException;
import app.finplan.exception.NotFoundException;
import app.finplan.validation.ValidPassword;
import jakarta.validation.ConstraintViolation;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> notFound(AuthException ex){
        HttpStatus status;

        switch (ex.getCode()) {
            case AuthException.EMAIL_EXISTS,
                    AuthException.USER_NOT_FOUND,
                    AuthException.EMAIL_ALREADY_CONFIRMED,
                    AuthException.INVALID_CONFIRMATION_CODE -> {
                status = HttpStatus.CONFLICT;
            }
            case AuthException.INVALID_CREDENTIALS -> {
                status = HttpStatus.UNAUTHORIZED;
            }
            case AuthException.EMAIL_NOT_CONFIRMED -> {
                status = HttpStatus.FORBIDDEN;
            }
            default -> {
                status = HttpStatus.BAD_REQUEST;
            }
        }

        var pd = ProblemDetail.forStatus(status);
        pd.setTitle(ex.getMessage());
        pd.setDetail(ex.getMessage());
        pd.setProperty("code", ex.getCode());

        return ResponseEntity.status(status).body(pd);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(NotFoundException ex){
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("code", "not_found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation error");

        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage(),
                        "code", resolveErrorCode(fe) // добавим
                ))
                .toList();

        pd.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(pd);
    }

    private String resolveErrorCode(FieldError fe) {
        if (fe.contains(ValidPassword.class)) {
            // Вытаскиваем из аннотации
            ValidPassword annot = (ValidPassword) fe.unwrap(ConstraintViolation.class)
                    .getConstraintDescriptor()
                    .getAnnotation();
            return annot.code();
        }

        return "validation_error"; // общий код
    }
}
