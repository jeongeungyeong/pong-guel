package org.example.pongguel.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.exception.BadRequestException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.InternalServerException;
import org.example.pongguel.jwt.JwtTokenProvider;
import org.example.pongguel.jwt.dto.JwtTokenDto;
import org.example.pongguel.user.domain.KakaoToken;
import org.example.pongguel.user.domain.Role;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.dto.*;
import org.example.pongguel.user.repository.KakaoTokenRepository;
import org.example.pongguel.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
public class KakaoSignInService {
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
    @Value("${kakao.user_info_url}")
    private String userInfoUrl;

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 인증코드 발급 URL 생성
    public String getKakaoAuthUrl(){
        return String.format("https://%s/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                kauthBaseUrl, clientId, redirectUrl);
    }

    // 카카오 로그인 및 회원가입 진행 (카카오 토큰 저장)
    public LoginResult processKakaoLongin(String code){
        // 1. 카카오 토큰 발급
        KakaoTokenInfo kakaoTokenInfo = getAccessToken(code);
        // 2. 액세스 토큰으로 사용자 정보 가져오기
        KakaoUserInfoAndToken kakaoUserInfoAndToken = getKakaoUserInfo(kakaoTokenInfo);
        // 3. 회원가입 및 로그인 진행하기
        UserInfoResponse userInfoResponse = processKakaoUser(kakaoUserInfoAndToken);
        // 4. 카카오 사용자 조회 및 회원가입
        HttpStatus status = userInfoResponse.isNewUser() ? HttpStatus.CREATED : HttpStatus.OK;
        String message = userInfoResponse.isNewUser() ? "회원가입 및 카카오 로그인에 성공했습니다!" : "카카오 로그인에 성공했습니다.";
        // jwt 토큰 발급
        String jwtAccessToken = jwtTokenProvider.createAccessToken(userInfoResponse.accountEmail());
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken(userInfoResponse.accountEmail());
        JwtTokenDto jwtTokenDto = new JwtTokenDto(jwtAccessToken, jwtRefreshToken);
        // LoginResult 객체 생성 및 반환
        return new LoginResult(status,new LoginResponse(message,userInfoResponse,jwtTokenDto));
    }

    // 카카오 accessToken 발급
    private KakaoTokenInfo getAccessToken(String code) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(kauthBaseUrl)
                        .path("/token")
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(createTokenRequestBody(code)))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    String kakao_access_token= jsonNode.path("access_token").asText();
                    Long kakao_access_token_expires_in = jsonNode.path("expires_in").asLong();
                    String kakao_refresh_token= jsonNode.path("refresh_token").asText();
                    Long kakao_refresh_token_expires_in = jsonNode.path("refresh_token_expires_in").asLong();
                    return new KakaoTokenInfo(kakao_access_token,kakao_access_token_expires_in,kakao_refresh_token,kakao_refresh_token_expires_in);
                })
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("카카오 accessToken 발급 실패. 상태 코드: {}, 응답: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
                    return new BadRequestException(ErrorCode.KAKAO_ACCESS_TOKEN_BAD_REQUEST);
                })
                .onErrorMap(Exception.class, e -> {
                    log.error("카카오 accessToken 발급 중 예상치 못한 오류 발생", e);
                    return new InternalServerException(ErrorCode.KAKAO_ACCESS_TOKEN_INTERNAL_SERVER_ERROR);
                })
                .block();
    }

    // 카카오 사용자 정보 가져오기
    private KakaoUserInfoAndToken getKakaoUserInfo(KakaoTokenInfo kakaoTokenInfo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(userInfoUrl)
                        .path("/user/me")
                        .build())
                .header("Authorization","Bearer " + kakaoTokenInfo.kakaoAccessToken())
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(jsonNode -> log.info("카카오 응답: {}", jsonNode.toString()))  // 응답 로깅 추가
                .map(jsonNode -> {
                    Long id = jsonNode.path("id").asLong();
                    String email = jsonNode.path("kakao_account").path("profile").path("email").asText();
                    String nickname = jsonNode.path("properties").path("nickname").asText();
                    String thumbnail_image_url = jsonNode.path("properties").path("thumbnail_image_url").asText();
                    return new KakaoUserInfoAndToken(id, email, nickname, thumbnail_image_url, kakaoTokenInfo);
                })
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("카카오 사용자 정보 조회에 실패했습니다. 상태코드: {}, 응답:{}", e.getRawStatusCode(), e.getResponseBodyAsString());
                    return new BadRequestException(ErrorCode.KAKAO_ACCESS_TOKEN_BAD_REQUEST);
                })
                .block();
    }

    // 카카오 accessToken 발급 body
    private MultiValueMap<String,String> createTokenRequestBody(String code) {
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);
        return body;
    }

    // 카카오 로그인 진행 및 토큰 저장
    private UserInfoResponse processKakaoUser(KakaoUserInfoAndToken kakaoUserInfoAndToken){
        // 신규회원 유무 확인
        boolean isNewUser = !userRepository.existsByAccountEmail(kakaoUserInfoAndToken.email());
        User user = userRepository.findByAccountEmail(kakaoUserInfoAndToken.email())
                .orElseGet(()->{
                    // 회원정보가 없다면 회원가입 진행
                    User newUser = User.builder()
                            .kakaoId(kakaoUserInfoAndToken.id())
                            .accountEmail(kakaoUserInfoAndToken.email())
                            .nickname(kakaoUserInfoAndToken.nickname())
                            .profileImage(kakaoUserInfoAndToken.thumbnail_image_url())
                            .role(Role.ROLE_USER)
                            .build();
                    // 카카오 토큰 저장
                    KakaoToken kakaoToken = KakaoToken.builder()
                            .kakaoAccessToken(kakaoUserInfoAndToken.kakaoTokenInfo().kakaoAccessToken())
                            .kakaoRefreshToken(kakaoUserInfoAndToken.kakaoTokenInfo().kakaoRefreshToken())
                            .kakaoAccessTokenExpiresIn(kakaoUserInfoAndToken.kakaoTokenInfo().kakaoAccessTokenExpiresIn())
                            .kakaoRefreshTokenExpiresIn(kakaoUserInfoAndToken.kakaoTokenInfo().kakaoRefreshTokenExpiresIn())
                            .kakaoCreatedAt(LocalDateTime.now())
                            .user(newUser)
                            .build();
                    kakaoTokenRepository.save(kakaoToken);
                    return userRepository.save(newUser);
                });
        // 액세스 토큰 업데이트
        KakaoToken kakaoToken = kakaoTokenRepository.findByUser_userId(user.getUserId())
                .orElseThrow(()-> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        kakaoToken.updateKakaoToken(kakaoUserInfoAndToken.kakaoTokenInfo().kakaoAccessToken(),
                kakaoUserInfoAndToken.kakaoTokenInfo().kakaoAccessTokenExpiresIn(),
                kakaoUserInfoAndToken.kakaoTokenInfo().kakaoRefreshToken(),
                kakaoUserInfoAndToken.kakaoTokenInfo().kakaoRefreshTokenExpiresIn(),
                LocalDateTime.now(),
                false);
        // 변한 카카오 액세스 토큰 저장
        kakaoTokenRepository.save(kakaoToken);
        return new UserInfoResponse(user.getAccountEmail(), user.getNickname(),
                user.getProfileImage(),user.getRole(),isNewUser);
    }
}
