package kr.co.mcedu.user.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");
    private final String cd;
    UserRole(String cd) {
        this.cd = cd;
    }
}