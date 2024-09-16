package org.example.pongguel.jwt.dto;

public record JwtTokenDto(String accessToken,
                          String refreshToken) {
}
