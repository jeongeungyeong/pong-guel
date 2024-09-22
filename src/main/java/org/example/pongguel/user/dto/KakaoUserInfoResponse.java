package org.example.pongguel.user.dto;

import org.example.pongguel.user.domain.Role;

public record KakaoUserInfoResponse(String accountEmail,
                                    String nickname,
                                    String profile_image_url,
                                    Role role,
                                    boolean isNewUser) {
}
