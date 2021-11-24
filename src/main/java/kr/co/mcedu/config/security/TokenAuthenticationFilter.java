package kr.co.mcedu.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final WebUserService webUserService;
    @Value("${security.ignore-url}")
    private final List<String> securityIgnore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.parseTokenCookie(request, TokenType.ACCESS_TOKEN);
        boolean needRefresh = false;
        try {
            if (jwtTokenProvider.validateToken(accessToken)) {
                this.setAuthentication(accessToken);
                long userSeq = jwtTokenProvider.getUserSeq(accessToken);
                if(webUserService.isRefreshedUser(userSeq)) {
                    needRefresh = true;
                }
            } else {
                needRefresh = true;
            }
        } catch (ExpiredJwtException e) {
            jwtTokenProvider.deleteTokenCookie(response, TokenType.ACCESS_TOKEN);
            needRefresh = true;
        }
        if (needRefresh) {
            accessToken = SessionUtils.refreshProcess(request, response);
        }

        if (!StringUtils.isEmpty(accessToken)) {
            this.setAuthentication(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return securityIgnore.stream()
                             .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));

    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}