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
    @Operation(summary = "카카오 로그아웃 기능입니다.", description = "서비스 로그아웃뿐만 아니라 카카오톡 액세스토큰도 만료합니다.")
    public ResponseEntity<String> logout(HttpServletRequest request) {
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
