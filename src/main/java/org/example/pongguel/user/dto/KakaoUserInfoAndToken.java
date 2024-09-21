package org.example.pongguel.user.dto;

public record KakaoUserInfoAndToken(Long id,
                                    String email,
                                    String nickname,
                                    String thumbnail_image_url,
                                    KakaoTokenInfo kakaoTokenInfo

) {
}
