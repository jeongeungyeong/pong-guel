package org.example.pongguel.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.BadRequestException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.InternalServerException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.KakaoToken;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.dto.PongUserInfo;
import org.example.pongguel.user.dto.UnlinkedUserDto;
import org.example.pongguel.user.repository.KakaoTokenRepository;
import org.example.pongguel.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Value("${kakao.user_base_url}")
    private String userBaseUrl;
    private final UserRepository userRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;
    private final WebClient webClient;

    // 사용자 정보 조회 (마이페이지)
    public PongUserInfo getUserInfo(String token){
        // 1. 토큰 유효성 검사 및 사용자 정보 가져오기
        User user = validateUser.getUserFromToken(token);
        // 2. 사용자가 저장한 책의 수
        List<Book> savedBooks = bookRepository.findByUser_UserId(user.getUserId());
        long savedBooksCount = savedBooks.size();
        // 3. 사용자가 작성한 노트 수
        List<Note> notes = noteRepository.findAllByUser_userIdAndIsDeletedOrderByNoteCreatedAtDesc(user.getUserId(), false);
        long notesCount = notes.size();

        // 4. PongUserInfo 생성
        return new PongUserInfo(
                user.getAccountEmail(),
                user.getNickname(),
                user.getProfileImage(),
                savedBooksCount,
                notesCount
        );
    }

    // 유저 연결 끊기
    public UnlinkedUserDto leave(String token){
        try {
            // 1. 토큰 유효성 검사 및 사용자 권한 인증
            User user = validateUser.getUserFromToken(token);
            // 2. 카카오 액세스 토큰 찾기
            KakaoToken kakaoToken = kakaoTokenRepository.findByUser_userId(user.getUserId())
                    .orElseThrow(()->new BadRequestException(ErrorCode.KAKAO_TOKEN_NOT_FOUND));
            // 3. 카카오 계정 연결 끊기
            unlink(kakaoToken.getKakaoAccessToken());
            // 4. 사용자의 모든 노트 삭제
            noteRepository.deleteAllNotesByUser_UserId(user.getUserId());
            // 5. 사용자의 모든 책 삭제
            bookRepository.deleteAllBooksByUser_UserId(user.getUserId());
            //6. 사용자 카카오 토큰 삭제
            kakaoTokenRepository.delete(kakaoToken);
            //7. 사용자 서버 삭제
            userRepository.delete(user);
            return new UnlinkedUserDto("회원 탈퇴가 성공적으로 이뤄졌습니다.", user.getKakaoId());
        } catch (Exception e) {
            log.error("회원 탈퇴 처리 중 오류 발생", e);
            throw new InternalServerException(ErrorCode.KAKAO_LEAVE_INTERNAL_SERVER_ERROR);
        }
    }

    // 카카오 연결 끊기
    private void unlink(String accessToken){
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(userBaseUrl)
                        .path("/user/unlink")
                        .build())
                .header("Authorization","Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> jsonNode.path("id").asLong())
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("카카오 연결 끊기 실패. 상태 코드: {}, 응답: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
                    return new BadRequestException(ErrorCode.KAKAO_ACCESS_TOKEN_BAD_REQUEST);
                })
                .block();
    }
}
