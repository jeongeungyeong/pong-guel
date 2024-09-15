package org.example.pongguel.user.dto;

import org.example.pongguel.user.domain.Grade;

public record UserInfoResponse(String accountEmail,
                               String nickname,
                               String profile_image_url,
                               Grade grade,
                               boolean isNewUser) {
}
