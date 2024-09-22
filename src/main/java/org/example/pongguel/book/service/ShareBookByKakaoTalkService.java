package org.example.pongguel.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.dto.ShareBookResponse;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.feign.service.KakaoTalkService;
import org.example.pongguel.jwt.JwtTokenProvider;
import org.example.pongguel.user.domain.KakaoToken;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.repository.KakaoTokenRepository;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShareBookByKakaoTalkService {
    @Value("${share_book.share_base_url}")
    private String shareBaseUrl;

    private final BookRepository bookRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final ValidateUser validateUser;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoTalkService kakaoTalkService;

    /* 책 카카오톡으로 공유하기
    * shareToFriends true = 친구에게 공유
    * shareToFriends false = 나에게 공유
    *  */
    public ShareBookResponse shareBook(String token, Long bookId, boolean shareToFriends) {
        // 1. 사용자 토큰 확인 및 정보 갖고 오기
        User user = validateUser.getUserFromToken(token);
        // 2. 사용자의 카카오 토큰 확인 및 카카오 액세스 토큰 가져오기
        KakaoToken kakaoToken = getKakaoToken(user);
        // 3. 책 정보 가져오기
        Book book = getBook(bookId);
        // 4. 공유 토큰 생성하기 (10분간 유효)
        String shareToken = jwtTokenProvider.createShareToken(user.getAccountEmail(), bookId);
        // 5. 공유 url 생성하기
        String shareUrl = createSharedBookUrl(shareToken);
        //6. 카카오톡 메시지 보내기
        boolean messageSent = sendKakaoMessage(kakaoToken, book, user, shareUrl, shareToFriends);
        //7. 응답 객체 생성 및 반환
        return createShareBookResponse(messageSent, shareToken, shareUrl, book, user);
    }

    private KakaoToken getKakaoToken(User user) {
        return kakaoTokenRepository.findByUser_userId(user.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.KAKAO_TOKEN_NOT_FOUND));
    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
    }

    private boolean sendKakaoMessage(KakaoToken kakaoToken, Book book, User user, String shareUrl, boolean shareToFriends) {
        String title = book.getBookTitle();
        String description = user.getNickname() + "님께서 " + book.getBookTitle() + " 관련 퐁글이를 공유하셨어요! 퐁글이는 10분 동안 유효합니다!";
        String imageUrl = book.getImageUrl();

        if (shareToFriends) {
            return kakaoTalkService.sendKakaoTalkToFriends(kakaoToken.getKakaoAccessToken(), title, description, imageUrl, shareUrl);
        } else {
            return kakaoTalkService.sendKakaoTalkToMe(kakaoToken.getKakaoAccessToken(), title, description, imageUrl, shareUrl);
        }
    }

    // 카카오톡 메시지 객체에 넣은 contents 만들기
    private ShareBookResponse createShareBookResponse(boolean messageSent, String shareToken, String shareUrl, Book book, User user) {
        String message = messageSent ? "책이 성공적으로 공유되고 카카오톡 메시지가 전송되었습니다."
                : "책이 공유되었지만 카카오톡 메시지 전송에 실패했습니다.";

        return new ShareBookResponse(
                message,
                shareToken,
                shareUrl,
                book.getBookId(),
                book.getBookTitle(),
                user.getNickname(),
                user.getAccountEmail(),
                messageSent
        );
    }
    // 외부 공유 URL 생성
    private String createSharedBookUrl(String token) {
        return String.format("%s/%s", shareBaseUrl, token);
    }
}
