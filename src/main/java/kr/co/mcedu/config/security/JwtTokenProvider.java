package kr.co.mcedu.config.security;

import io.jsonwebtoken.*;
import kr.co.mcedu.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secret;
    private String encodedSecretKey;

    @PostConstruct
    public void init() {
        encodedSecretKey = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * JWT 생성
     *
     * @param tokenType token type
     * @param data 필요한 데이터, 정책서 확인
     * @return JWT
     */
    public String createToken(TokenType tokenType, Map<String, Object> data) {
        Date now = new Date();
        Date expiration = new Date();
        expiration.setTime(now.getTime() + tokenType.getValidMilliseconds());
        return Jwts.builder()
                   .setIssuedAt(now)
                   .setClaims(data)
                   .setExpiration(expiration)
                   .signWith(SignatureAlgorithm.HS512, encodedSecretKey)
                   .compact();
    }

    /**
     * JWT 검증
     *
     * @param token 검증할 토큰
     * @return 유효여부
     * @throws ExpiredJwtException 만료 Exception
     */
    public boolean validateToken(String token) throws ExpiredJwtException {
        if (StringUtils.isNotEmpty(token)) {
            try {
                Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token);
                return true;
            } catch (SignatureException e) {
                log.error("Invalid JWT signature", e);
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token", e);
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token");
                throw e;
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token", e);
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty.", e);
            }
        }
        return false;
    }

    /**
     * Cookie 에서 JWT 가져오기
     * @param request request
     * @param tokenType token type
     * @return cookie에 해당하는 JWT 반환
     */
    public String parseTokenCookie(HttpServletRequest request, TokenType tokenType){
        final AtomicReference<String> token = new AtomicReference<>("");
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return "";
        }

        Arrays.stream(cookies).forEach(it -> {
            if (tokenType.getCookieName().equals(it.getName())) {
                token.set(it.getValue());
            }
        });
        return token.get();
    }

    /**
     * Cookie 에서 JWT 삭제
     * @param response response
     * @param tokenType token type
     */
    public void deleteTokenCookie(HttpServletResponse response, TokenType tokenType) {
        Cookie cookie = new Cookie(tokenType.getCookieName(), null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 토큰에 있는 userSeq 가져오기
     * @param token 토큰
     * @return userSeq, 없을 경우 0
     */
    public long getUserSeq(String token) {
        Claims claims = Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token).getBody();
        Long userSeq = claims.get("userSeq", Long.class);
        return Optional.ofNullable(userSeq).orElse(0L);
    }
}
