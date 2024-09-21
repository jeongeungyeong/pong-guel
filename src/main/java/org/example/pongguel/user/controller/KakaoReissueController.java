package org.example.pongguel.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.user.service.KakaoTokenReissueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao-token")
@Tag(name="KAKAO_TOKEN",description = "카카오 토큰에 관련된 Api입니다.")
public class KakaoReissueController {
    private final KakaoTokenReissueService kakaoTokenReissueService;
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    @Operation(summary = "카카오 액세스토큰 재발급", description = "카카오 리프레시 토큰으로 액세스토큰을 재발급 받습니다.")
    public ResponseEntity<String> getKakaoAuthLogoutUrl(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        String reissueToken = kakaoTokenReissueService.reissueToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(reissueToken);
    }
}
