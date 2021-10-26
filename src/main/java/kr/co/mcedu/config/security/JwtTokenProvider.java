package kr.co.mcedu.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secret;
    private String encodedSecretKey;
    private long refreshTokenValidMilliseconds = 1000L * 60 * 60 * 24 * 14; // 2주

    @PostConstruct
    public void init() {
        encodedSecretKey = Base64.getEncoder().encodeToString(secret.getBytes());
    }
}
