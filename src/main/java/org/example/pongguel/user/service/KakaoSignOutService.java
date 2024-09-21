package org.example.pongguel.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.exception.BadRequestException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.InternalServerException;
import org.example.pongguel.user.domain.KakaoToken;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.repository.KakaoTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KakaoSignOutService {
    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_url}")
    private String redirectUrl;
    @Value("${kakao.logout_redirect_url}")
    private String logoutRedirectUrl;
    @Value("${kakao.user_base_url}")
    private String userBaseUrl;
    @Value("${kakao.kauth_base_url}")
    private String kauthBaseUrl;

    private final WebClient webClient;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final ValidateUser validateUser;

    // 로그아웃
    public String logout(String token){
        try {
            // 1. 토큰 유효성 검사 및 사용자 권한 인증
            User user = validateUser.getUserFromToken(token);
            // 2. 카카오 액세스 토큰 찾기
            KakaoToken kakaoToken = kakaoTokenRepository.findByUser_userId(user.getUserId())
                    .orElseThrow(()->new BadRequestException(ErrorCode.KAKAO_TOKEN_NOT_FOUND));
            // 3. 카카오 API를 통해 액세스 토큰 만료
            expiredAccessToken(kakaoToken.getKakaoAccessToken());
            // 4. 서버 측 토큰 관리 (isAccessExpired=true로 변경)
            kakaoToken.accessExpired();
            // 5. 카카오 계정과 함께 로그아웃 URL로 리다이렉트
            return getKakaoAuthLogoutUrl();
        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생", e); // 에러코드 추가
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 카카오 로그아웃 - 액세스 토큰 만료
    private void expiredAccessToken(String accessToken) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(userBaseUrl)
                        .path("/user/logout")
                        .build())
                .header("Authorization","Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> jsonNode.path("id").asLong())
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("카카오 accessToken 만료 실패. 상태 코드: {}, 응답: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
                    return new BadRequestException(ErrorCode.KAKAO_ACCESS_TOKEN_BAD_REQUEST);
                })
                .block();
    }

    // 카카오 서비스와 함께 로그아웃 URL 생성
    private String getKakaoAuthLogoutUrl(){
        return String.format("https://%s/logout?response_type=code&client_id=%s&logout_redirect_uri=%s",
                kauthBaseUrl, clientId, logoutRedirectUrl);
    }
}
