package kr.co.mcedu.user.model;

import lombok.Getter;

@Getter
public enum UserAuthority {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String security;
    UserAuthority(String security) {
        this.security = security;
    }
}