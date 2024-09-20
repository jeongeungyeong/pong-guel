package org.example.pongguel.user.dto;

import org.example.pongguel.user.domain.Role;

public record UserInfoResponse(String accountEmail,
                               String nickname,
                               String profile_image_url,
                               Role role,
                               boolean isNewUser) {
}
