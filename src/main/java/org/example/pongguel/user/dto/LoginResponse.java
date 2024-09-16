package org.example.pongguel.user.dto;

public record LoginResponse(String message,
                            UserInfoResponse userInfoResponse,
                            JwtTokenDto jwtTokenDto) {
}
