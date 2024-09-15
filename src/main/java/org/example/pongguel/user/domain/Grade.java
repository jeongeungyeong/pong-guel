package org.example.pongguel.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    NORMAL, PREMIUM, ADMIN;
}
