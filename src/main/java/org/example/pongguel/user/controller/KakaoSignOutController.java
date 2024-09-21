package org.example.pongguel.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.user.service.KakaoSignOutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao-logout")
@Tag(name="KAKAO_LOGOUT",description = "카카오 로그아웃에 관련된 Api입니다.")
public class KakaoSignOutController {
    private final KakaoSignOutService kakaoSignOutService;
    private final JwtUtil jwtUtil;

    // 카카오 로그아웃 페이지로 리다이렉트
    @GetMapping("/sign-out")
    @Operation(summary = "카카오 액세스 토큰 재발급", description = ".")
    public ResponseEntity<String> reissueToken(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        String kakaoAuthUtl = kakaoSignOutService.logout(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(kakaoAuthUtl))
                .build();
    }
}
