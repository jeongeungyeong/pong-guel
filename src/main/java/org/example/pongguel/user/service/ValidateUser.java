package org.example.pongguel.user.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtTokenProvider;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateUser {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 토큰 유효성 검사 및 유저 정보 조회
    public User getUserFromToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        String accountEmail = jwtTokenProvider.getUserEmail(token);
        return userRepository.findByAccountEmail(accountEmail)
                .orElseThrow(()->new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
