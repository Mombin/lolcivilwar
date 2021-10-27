package kr.co.mcedu.utils;

import kr.co.mcedu.config.security.JwtTokenProvider;
import kr.co.mcedu.config.security.TokenType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SessionUtils {
    private static JwtTokenProvider jwtTokenProvider;

    public SessionUtils(JwtTokenProvider jwtTokenProvider) {
        SessionUtils.setTokenProvider(jwtTokenProvider);
    }

    /**
     * static method jwtTokenProcider setter
     * @param jwtTokenProvider jwtTokenProvider Bean
     */
    private static void setTokenProvider(JwtTokenProvider jwtTokenProvider) {
        SessionUtils.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authentication 에 넣은 id 추출
     *
     * @return id
     */
    public static String getId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Request 에서 IP 추출
     *
     * @return ip
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return Optional.ofNullable(ip).orElse("");
    }

    /**
     * Refresh token 에서 userSeq 추출
     *
     * @return userSeq, 없을경우 0
     */
    public static long getUserSeq() {
        HttpServletRequest request = getRequest();
        final AtomicReference<String> token = new AtomicReference<>("");
        Arrays.stream(request.getCookies()).forEach(it -> {
            if (TokenType.REFRESH_TOKEN.getCookieName().equals(it.getName())) {
                token.set(it.getValue());
            }
        });
        return jwtTokenProvider.getUserSeq(token.get());
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}