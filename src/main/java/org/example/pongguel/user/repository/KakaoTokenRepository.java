package org.example.pongguel.user.repository;

import org.example.pongguel.user.domain.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken,Long> {
    //userId로 찾기
    Optional<KakaoToken> findByUser_userId(UUID userId);
}
