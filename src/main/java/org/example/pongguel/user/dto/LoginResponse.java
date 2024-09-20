package org.example.pongguel.user.dto;

import org.example.pongguel.jwt.dto.JwtTokenDto;

public record LoginResponse(String message,
                            UserInfoResponse userInfoResponse,
                            JwtTokenDto jwtTokenDto) {
}
