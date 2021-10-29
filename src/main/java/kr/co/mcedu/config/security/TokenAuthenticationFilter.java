package kr.co.mcedu.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${security.ignore-url}")
    private final List<String> securityIgnore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.parseTokenCookie(request, TokenType.ACCESS_TOKEN);
        boolean needRefresh = false;
        try {
            if (jwtTokenProvider.validateToken(accessToken)) {
                //TODO: authentication 생성
                // SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(accessToken)
            }
        } catch (ExpiredJwtException e) {
            jwtTokenProvider.deleteTokenCookie(response, TokenType.ACCESS_TOKEN);
            needRefresh = true;
        }
        if (needRefresh) {
            refreshProcess(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void refreshProcess(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.parseTokenCookie(request, TokenType.REFRESH_TOKEN);
        try {
            if (jwtTokenProvider.validateToken(refreshToken)) {
                // TODO: accessToken 생성
            }
        } catch (ExpiredJwtException e) {
            jwtTokenProvider.deleteTokenCookie(response, TokenType.REFRESH_TOKEN);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return securityIgnore.stream()
                             .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));

    }
}