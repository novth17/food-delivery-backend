package com.lilyhien.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// LoginRequestDto only holds what is needed to log in (email + password).
// It doesn't carry the user's role or ID because those aren't needed yet.
@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
