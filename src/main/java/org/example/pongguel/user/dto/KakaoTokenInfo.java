package org.example.pongguel.user.dto;

public record KakaoTokenInfo(String kakaoAccessToken,
                             Long kakaoAccessTokenExpiresIn,
                             String kakaoRefreshToken,
                             Long kakaoRefreshTokenExpiresIn) {
}
