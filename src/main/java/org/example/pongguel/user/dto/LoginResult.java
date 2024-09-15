package org.example.pongguel.user.dto;

import org.springframework.http.HttpStatus;

public record LoginResult(HttpStatus status, LoginResponse loginResponse) {
}
