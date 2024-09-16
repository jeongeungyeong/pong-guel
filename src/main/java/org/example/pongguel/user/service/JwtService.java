package org.example.pongguel.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.jwt.JwtTokenProvider;
import org.example.pongguel.exception.BadRequestException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.user.dto.JwtTokenDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JwtService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    // 토큰 재발급
    public JwtTokenDto reissueTokenByRefreshToken(String refreshToken) {
        // 1. 토큰 유효성 검사
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new BadRequestException(ErrorCode.JWT_REFRESH_INVALID_TOKEN);
            }
            String email = jwtTokenProvider.getUserEmail(refreshToken);
            String savedRefreshToken = redisTemplate.opsForValue().get("RT:" + email);

            if (savedRefreshToken == null ||
            !savedRefreshToken.equals(refreshToken)) {
                throw new BadRequestException(ErrorCode.JWT_REFRESH_BAD_REQUEST);
            }
            // 새로운 액세스 토큰 생성
            String newAccessToken = jwtTokenProvider.createAccessToken(email);
            return new JwtTokenDto(newAccessToken,refreshToken);
    }
}
