package org.example.pongguel.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.exception.BadRequestException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.user.domain.KakaoToken;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.dto.KakaoTokenInfo;
import org.example.pongguel.user.repository.KakaoTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KakaoTokenReissueService {
    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.kauth_base_url}")
    private String kauthBaseUrl;

    private final WebClient webClient;
    private final ValidateUser validateUser;
    private final KakaoTokenRepository kakaoTokenRepository;

    // 카카오 액세스 토큰 재발급
    public String reissueToken(String token) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. 카카오 토큰 정보 찾기
        KakaoToken kakaoToken = kakaoTokenRepository.findByUser_userId(user.getUserId())
                .orElseThrow(()->new BadRequestException(ErrorCode.KAKAO_TOKEN_NOT_FOUND));
        // 3. 카카오 액세스 토큰 재발급
        KakaoTokenInfo kakaoTokenInfo = reissueAccessToken(kakaoToken.getKakaoRefreshToken());
        // 4. 액세스 토큰 업데이트
        kakaoToken.updateKakaoToken(kakaoTokenInfo.kakaoAccessToken(),
               kakaoTokenInfo.kakaoAccessTokenExpiresIn(),
                kakaoTokenInfo.kakaoRefreshToken(),
                kakaoTokenInfo.kakaoRefreshTokenExpiresIn(),
                LocalDateTime.now(),
                false);
        kakaoTokenRepository.save(kakaoToken);
        return "카카오 액세스 토큰이 성공적으로 발급됐습니다.";
    }

    // 카카오 액세스 토큰 재발급 url
    private KakaoTokenInfo reissueAccessToken(String kakaoRefreshToken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(kauthBaseUrl)
                        .path("/token")
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(createReissueTokenRequestBody(kakaoRefreshToken)))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    String new_kakao_access_token= jsonNode.path("access_token").asText();
                    Long new_kakao_access_token_expires_in = jsonNode.path("expires_in").asLong();
                    String kakao_refresh_token= jsonNode.path("refresh_token").asText();
                    Long kakao_refresh_token_expires_in = jsonNode.path("refresh_token_expires_in").asLong();
                    return new KakaoTokenInfo(new_kakao_access_token,new_kakao_access_token_expires_in,kakao_refresh_token,kakao_refresh_token_expires_in);
                })
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("카카오 accessToken 갱신 실패. 상태 코드: {}, 응답: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
                    return new BadRequestException(ErrorCode.KAKAO_ACCESS_TOKEN_BAD_REQUEST);
                })
                .block();
    }

    // 카카오 accessToken 재발급 body
    private MultiValueMap<String,String> createReissueTokenRequestBody(String kakaoRefreshToken) {
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", kakaoRefreshToken);
        return body;
    }
}
