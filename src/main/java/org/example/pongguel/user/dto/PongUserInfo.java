package org.example.pongguel.user.dto;

public record PongUserInfo(String accountEmail,
                           String nickname,
                           String profileImage,
                           Long bookCount,
                           Long noteCount) {
}
