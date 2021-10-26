package kr.co.mcedu.config.security;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS_TOKEN("acToken", 1000L * 60 * 15), // 15분
    REFRESH_TOKEN("cv_rf_tk",1000L * 60 * 60 * 24 * 14); // 2주
    private final String cookieName;
    private final long validMilliseconds;
    TokenType(String cookieName, long validMilliseconds) {
        this.cookieName = cookieName;
        this.validMilliseconds = validMilliseconds;
    }
}
