package app.finplan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AccountException implements BusinessExCode {
    NOT_FOUND(
            "account_not_found",
            "Account not found",
            HttpStatus.NOT_FOUND
    ),
    LESS_ZERO(
            "account_less_zero",
            "Account less zero",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus status;
}
