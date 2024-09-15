package org.example.pongguel.user.repository;

import org.example.pongguel.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // 이메일로 사용자 찾기
    Optional<User> findByAccountEmail(String accountEmail);
    // 신규회원 유무 확인
    boolean existsByAccountEmail(String accountEmail);
}
