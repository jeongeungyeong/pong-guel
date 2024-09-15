package org.example.pongguel.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.exception.*;
import org.example.pongguel.user.domain.Grade;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.dto.*;
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
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.pongguel.exception.ErrorCode.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;
    @Value("${kakao.oauth_uri}")
    private String oauthUri;
    @Value("${kakao.oauth_token_uri}")
    private String oauthTokenUri;
    @Value("${kakao.oauth_user_uri}")
    private String oauthUserUri;

    private final WebClient webClient;
    private final UserRepository userRepository;

    // 카카오 인증코드 발급 URL 생성
    public String getKakaoAuthUrl(){
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s",
                oauthUri, clientId, redirectUri);
    }

    // 카카오 로그인 및 회원가입 진행
    public LoginResult processKakaoLongin(String code){
        String accessToken = getAccessToken(code);
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);
        UserInfoResponse userInfoResponse = processKakaoUser(kakaoUserInfo);

        HttpStatus status = userInfoResponse.isNewUser() ? HttpStatus.CREATED : HttpStatus.OK;
        String message = userInfoResponse.isNewUser() ? "회원가입 및 카카오 로그인에 성공했습니다!" : "카카오 로그인에 성공했습니다.";
        return new LoginResult(status,new LoginResponse(message,userInfoResponse));
    }

    // 카카오 accessToken 발급
    private String getAccessToken(String code){
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code"); // 인증방식 'authorization_code로 지정
        body.add("client_id", clientId); // 클라이언트 ID 추가
        body.add("redirect_uri", redirectUri); // 리다이렉트 ID 추가
        body.add("code", code); // 인증 코드 추가

        // WebClient를 사용하여 POST 요청
        try {
            return webClient.post()
                    // 토큰 요청 URI
                    .uri(oauthTokenUri)
                    // 컨텐츠 타입 설정
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    // 요청 본문에 form 데이터 추가
                    .body(BodyInserters.fromFormData(body))
                    // 응답 받기
                    .retrieve()
                    // 응답 본문을 JsonNode로 변환
                    .bodyToMono(JsonNode.class)
                    // JsonNode에서 access_token 추출
                    .map(jsonNode -> jsonNode.path("access_token").asText())
                    .block();
        } catch (WebClientResponseException e){
            log.error("카카오 accessToken 발급 실패. 상태 코드: {}, 응답: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw new BadRequestException(ErrorCode.KAKAO_ACCESS_TOKEN_BAD_REQUEST);
        } catch (Exception e) {
            log.error("카카오 accessToken 발급 중 예상치 못한 오류 발생", e);
            throw new InternalServerException(ErrorCode.KAKAO_ACCESS_TOKEN_INTERNAL_SERVER_ERROR);
        }
    }
    // 카카오 사용자 정보 가져오기
    private KakaoUserInfo getKakaoUserInfo(String accessToken){
       try {
           return webClient.post()
                   .uri(oauthUserUri)
                   .header("Authorization", "Bearer " + accessToken)
                   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                   .retrieve()
                   .bodyToMono(JsonNode.class)
                   .map(jsonNode -> {
                       Long id = jsonNode.path("id").asLong();
                       String email = jsonNode.path("kakao_account").path("email").asText();
                       String nickname = jsonNode.path("kakao_account").path("profile").path("nickname").asText();
                       String thumbnail_image_url = jsonNode.path("kakao_account").path("profile").path("thumbnail_image_url").asText();
                   return new KakaoUserInfo(id,email,nickname,thumbnail_image_url);
                   })
                   .block();
       } catch (WebClientResponseException e){
           log.error("카카오 사용자 정보 조회에 실패했습니다. 상태코드: {}, 응답:{}", e.getRawStatusCode(), e.getResponseBodyAsString());
           throw new InternalServerException(ErrorCode.KAKAO_USER_INFO_INTERNAL_SERVER_ERROR);
       }
    }

    // 카카오 로그인 진행
    private UserInfoResponse processKakaoUser(KakaoUserInfo kakaoUserInfo){
        // 신규회원 유무 확인
       boolean isNewUser = !userRepository.existsByAccountEmail(kakaoUserInfo.email());
        User user = userRepository.findByAccountEmail(kakaoUserInfo.email())
               .orElseGet(()->{
                   // 회원정보가 없다면 회원가입 진행
                   User newUser = User.builder()
                           .accountEmail(kakaoUserInfo.email())
                           .nickname(kakaoUserInfo.nickname())
                           .profileImage(kakaoUserInfo.thumbnail_image_url())
                           .grade(Grade.NORMAL)
                           .build();
                   return userRepository.save(newUser);
               });
       return new UserInfoResponse(user.getAccountEmail(), user.getNickname(),
               user.getProfileImage(),user.getGrade(),isNewUser);
    }
}
