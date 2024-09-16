package org.example.pongguel.user.dto;

public record JwtTokenDto(String accessToken,
                          String refreshToken) {
}
