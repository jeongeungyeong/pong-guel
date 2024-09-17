package org.example.pongguel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 클라이언트의 입력 값에 대한 일반적인 오류 (@PathVariable, @RequestParam가 잘못되었을 때)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "클라이언트의 입력 값을 확인해주세요."),
    // @RequestBody의 입력 값이 유효하지 않을 때
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    // 요청 본문 형식 오류 추가
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문의 형식이 올바르지 않습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 엔티티입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서비스 이용에 장애가 있습니다."),

    // 카카오 회원가입 및 로그인
    //400
    KAKAO_ACCESS_TOKEN_BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 카카오 access_token 파라미터 요청입니다."),
    KAKAO_USER_INFO_BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 카카오 사용자정보 파라미터 요청입니다."),
    //401
    KAKAO_ACCESS_TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인가코드 인증에 실패했습니다."),
    KAKAO_USER_INFO_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"카카오 액세스 토큰이 만료되었거나 유효하지 않습니다."),
    //403
    KAKAO_ACCESS_TOKEN_FORBIDDEN(HttpStatus.FORBIDDEN,"카카오 로그인이 비활성화됐습니다"),
    KAKAO_USER_INFO_FORBIDDEN(HttpStatus.FORBIDDEN,"카카오 정보 요청 권한이 없습니다."),
    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    //409
    PROCESS_USER_CONFLICT(HttpStatus.CONFLICT,"이미 가입한 회원입니다."),
    //500
    KAKAO_ACCESS_TOKEN_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"OAuth 서버 일시적 오류가 생겼습니다."),
    KAKAO_USER_INFO_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"카카오서버 내부의 일시적 오류가 생겼습니다."),
    PROCESS_USER_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"데이터베이스 오류가 생겼습니다."),

    // jwt 발급
    //400
    JWT_REFRESH_INVALID_TOKEN(HttpStatus.BAD_REQUEST,"리프레시 토큰이 유효하지 않습니다."),
    JWT_REFRESH_BAD_REQUEST(HttpStatus.BAD_REQUEST,"리프레시 토큰이 일치하지 않습니다."),
    //401
    JWT_REFRESH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"리프레시 토큰 재발급 권한이 없습니다."),
    JWT_INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"권한이 없는 토큰입니다."),

    // 책
    //400
    BOOK_BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 쿼리 요청입니다."),
    BOOK_INVALID_VALUE(HttpStatus.BAD_REQUEST,"잘못된 파라미터의 값입니다."),
    BOOK_ENCODING_ERROR(HttpStatus.BAD_REQUEST,"잘못된 형식의 인코딩입니다."),
    //404
    BOOK_INVALID_SEARCH(HttpStatus.NOT_FOUND,"존재하지 않는 검색 api입니다."),
    BOOK_SAVED_NOT_FOUND(HttpStatus.NOT_FOUND,"저장되지 않은 책입니다."),
    BOOK_LIKED_NOT_FOUND(HttpStatus.NOT_FOUND,"좋아요하지 않은 책입니다."),
    //409
    BOOK_LIKE_CONFLICT(HttpStatus.CONFLICT,"이미 좋아요를 누른 책입니다."),
    //500
    BOOK_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"시스템 오류가 발생했습니다.");

    //공통
    private final HttpStatus status;
    private final String message;
}
