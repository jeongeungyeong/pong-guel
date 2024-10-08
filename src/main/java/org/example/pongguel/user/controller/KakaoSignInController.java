package org.example.pongguel.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.user.dto.LoginResponse;
import org.example.pongguel.user.dto.LoginResult;
import org.example.pongguel.user.service.KakaoSignInService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
@Tag(name="KAKAO_LOGIN",description = "카카오 로그인에 관련된 Api입니다.")
public class KakaoSignInController {
    private final KakaoSignInService kakaoSignInService;

    // 카카오 로그인 페이지로 리다이렉트
    @GetMapping("/sign-in")
    public ResponseEntity<Void> redirectToKakaoAuth(){
        String kakaoAuthUrl = kakaoSignInService.getKakaoAuthUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(kakaoAuthUrl))
                .build();
    }

    // 카카오 로그인
    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인",
            description = "1. 인가코드를 바탕으로 accessToken을 발급받기" +
                    "2.사용자 정보를 kakao로 받아오기" +
                    "3.사용자 정보가 없을 시 회원가입 동시진행")
    public ResponseEntity<LoginResponse> kakaoLoginCallback(@RequestParam("code") String code) {
        LoginResult result = kakaoSignInService.processKakaoLongin(code);
        return ResponseEntity.status(result.status()).body(result.loginResponse());
    }
}
