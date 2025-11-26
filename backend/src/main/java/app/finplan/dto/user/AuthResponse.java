package app.finplan.dto.user;

import app.finplan.model.UserRole;

public record AuthResponse(String token, String email, UserRole role) {

}
