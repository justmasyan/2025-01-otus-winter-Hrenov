package ru.otus.hw.security;

import lombok.Getter;

@Getter
public enum UserRole {
    UNDEFINED(-1),
    ADMIN(0),
    MANAGER(1),
    USER(2);

    private final Integer code;

    UserRole(Integer code) {
        this.code = code;
    }
}
