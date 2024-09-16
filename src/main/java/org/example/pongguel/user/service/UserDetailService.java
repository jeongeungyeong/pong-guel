package org.example.pongguel.user.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.BadRequestException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.domain.CustomUserDeatils;
import org.example.pongguel.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDeatils loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByAccountEmail(email)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDeatils(user);
    }
}
