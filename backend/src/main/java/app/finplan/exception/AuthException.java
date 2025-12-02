package app.finplan.exception;

public class AuthException extends RuntimeException {
    public static final String BAD_REQUEST = "bad_request";
    public static final String EMAIL_EXISTS = "email_exists";
    public static final String EMAIL_ALREADY_CONFIRMED = "email_already_confirmed";
    public static final String INVALID_CONFIRMATION_CODE = "invalid_confirmation_code";
    public static final String USER_NOT_FOUND = "user_not_found";
    public static final String EMAIL_NOT_CONFIRMED = "email_not_confirmed";
    public static final String INVALID_CREDENTIALS = "invalid_credentials";

    String code = BAD_REQUEST;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
