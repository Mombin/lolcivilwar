package kr.co.mcedu.utils;

import io.jsonwebtoken.ExpiredJwtException;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.config.security.JwtTokenProvider;
import kr.co.mcedu.config.security.LolcwAuthentication;
import kr.co.mcedu.config.security.TokenType;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.model.GroupAuthDto;
import kr.co.mcedu.user.service.WebUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Component
public class SessionUtils {
    private static JwtTokenProvider jwtTokenProvider;
    private static WebUserService webUserService;

    public SessionUtils(JwtTokenProvider jwtTokenProvider, WebUserService webUserService) {
        SessionUtils.setTokenProvider(jwtTokenProvider);
        SessionUtils.setWebUserService(webUserService);
    }

    /**
     * static method jwtTokenProcider setter
     * @param jwtTokenProvider jwtTokenProvider Bean
     */
    private static void setTokenProvider(JwtTokenProvider jwtTokenProvider) {
        SessionUtils.jwtTokenProvider = jwtTokenProvider;
    }

    private static void setWebUserService(WebUserService webUserService) {
        SessionUtils.webUserService = webUserService;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof LolcwAuthentication)) {
            return 0;
        }
        return ((LolcwAuthentication) authentication).getUserSeq();
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }

    public static Map<Long, GroupAuthDto> getGroupAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof LolcwAuthentication)) {
            return Collections.emptyMap();
        }
        return ((LolcwAuthentication) authentication).getGroupAuth();
    }

    public static GroupAuthEnum getGroupAuth(Long groupSeq) {
        return Optional.ofNullable(getGroupAuth().get(groupSeq)).map(GroupAuthDto::getGroupAuth)
                       .orElse(GroupAuthEnum.NONE);
    }

    public static void groupManageableAuthCheck(Long groupSeq) throws AccessDeniedException {
        groupAuthorityCheck(groupSeq, GroupAuthEnum::isManageableAuth);
    }

    public static void groupAuthorityCheck(Long groupSeq, Predicate<GroupAuthEnum> authChecker) throws AccessDeniedException {
        if(!authChecker.test(getGroupAuth(groupSeq))) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }
    }

    public static String refreshAccessToken() {
        return refreshProcess(getRequest(), getResponse());
    }

    public static String refreshProcess(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.parseTokenCookie(request, TokenType.REFRESH_TOKEN);
        String accessToken = "";
        try {
            if (jwtTokenProvider.validateToken(refreshToken)) {
                accessToken = webUserService.getAccessToken(refreshToken);
                Cookie accessTokenCookie = new Cookie(TokenType.ACCESS_TOKEN.getCookieName(), accessToken);
                accessTokenCookie.setPath("/");
                response.addCookie(accessTokenCookie);
            } else {
                jwtTokenProvider.deleteTokenCookie(response, TokenType.REFRESH_TOKEN);
            }
        } catch (ExpiredJwtException e) {
            jwtTokenProvider.deleteTokenCookie(response, TokenType.REFRESH_TOKEN);
        } catch (ServiceException serviceException) {
            log.debug("", serviceException);
            jwtTokenProvider.deleteTokenCookie(response, TokenType.REFRESH_TOKEN);
        }
        return accessToken;
    }
}