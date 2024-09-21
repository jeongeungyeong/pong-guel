package org.example.pongguel.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "kakao_tokens")
public class KakaoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kakaoTokenId;

    private String kakaoAccessToken;
    private String kakaoRefreshToken;
    private Long kakaoAccessTokenExpiresIn;
    private Long kakaoRefreshTokenExpiresIn;

    @CreationTimestamp
    private LocalDateTime kakaoCreatedAt;

    @Column(name = "is_access_expired",nullable = false)
    private boolean isAccessExpired=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 토큰 업데이트
    public void updateKakaoToken(String kakaoAccessToken,
                                 Long kakaoAccessTokenExpiresIn,
                                 String kakaoRefreshToken,
                                 Long kakaoRefreshTokenExpiresIn,
                                 LocalDateTime kakaoCreatedAt,
                                 boolean isAccessExpired) {
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoAccessTokenExpiresIn = kakaoAccessTokenExpiresIn;
        this.kakaoRefreshToken = kakaoRefreshToken;
        this.kakaoRefreshTokenExpiresIn = kakaoRefreshTokenExpiresIn;
        this.kakaoCreatedAt = kakaoCreatedAt;
        this.isAccessExpired = isAccessExpired;
    }

    // 액세스 토큰 만료
    public void accessExpired() {
        this.isAccessExpired = true;
    }
}
