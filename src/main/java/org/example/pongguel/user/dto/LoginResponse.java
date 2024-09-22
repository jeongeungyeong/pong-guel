package org.example.pongguel.user.dto;

import org.example.pongguel.jwt.dto.JwtTokenDto;

public record LoginResponse(String message,
                            KakaoUserInfoResponse kakaoUserInfoResponse,
                            JwtTokenDto jwtTokenDto) {
}
