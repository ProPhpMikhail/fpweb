package app.finplan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CategoryException implements BusinessExCode {
    NOT_FOUND(
            "category_not_found",
            "Category not found",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus status;
}
