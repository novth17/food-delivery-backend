package com.lilyhien.requestDto;

import lombok.Data;

// LoginRequestDto only holds what is needed to log in (email + password).
// It doesn't carry the user's role or ID because those aren't needed yet.
@Data
public class LoginRequest {
    private String email;
    private String password;
}
