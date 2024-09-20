package org.example.pongguel.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.user.dto.LoginResponse;
import org.example.pongguel.user.dto.LoginResult;
import org.example.pongguel.user.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
@Tag(name="USER_KAKAO",description = "카카오 로그인/로그아웃에 관련된 Api입니다.")
public class KakaoController {
    private final KakaoService kakaoService;

    // 카카오 로그인 페이지로 리다이렉트
    @GetMapping("/login")
    @Operation(summary = "카카오 인가코드 발급", description = "카카오 인가코드 페이지로 리다이렉트됩니다.")
    public ResponseEntity<Void> redirectToKakaoAuth() {
        String kakaoAuthUtl = kakaoService.getKakaoAuthUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(kakaoAuthUtl))
                .build();
    }

    // 카카오 로그인
    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인",
            description = "1. 인가코드를 바탕으로 accessToken을 발급받기" +
                          "2.사용자 정보를 kakao로 받아오기" +
                          "3.사용자 정보가 없을 시 회원가입 동시진행")
    public ResponseEntity<LoginResponse> kakaoLoginCallback(@RequestParam("code") String code) {
        LoginResult result = kakaoService.processKakaoLongin(code);
        return ResponseEntity.status(result.status()).body(result.loginResponse());
    }

    // 카카오 로그아웃 페이지로 리다이렉트
    @GetMapping("/logout")
    @Operation(summary = "카카오계정과 함께 로그아웃", description = "카카오 로그아웃 페이지로 리다이렉트됩니다.")
    public ResponseEntity<Void> getKakaoAuthLogoutUrl() {
        String kakaoAuthUtl = kakaoService.getKakaoAuthLogoutUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(kakaoAuthUtl))
                .build();
    }
}
