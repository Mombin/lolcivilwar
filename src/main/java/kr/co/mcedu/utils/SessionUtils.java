package kr.co.mcedu.utils;

import io.jsonwebtoken.Claims;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.security.AccessTokenField;
import kr.co.mcedu.config.security.JwtTokenProvider;
import kr.co.mcedu.config.security.TokenType;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.model.GroupAuthDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

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

    public static Map<Long, GroupAuthDto> getGroupAuth() {
        Claims claim = jwtTokenProvider.getClaim(getAccessToken());
        List list = claim.get(AccessTokenField.GROUP_AUTH, List.class);
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, GroupAuthDto> groupAuthDtoMap = new HashMap<>();
        for (final Object o : list) {
            GroupAuthDto groupAuthDto = ModelUtils.map(o, GroupAuthDto.class);
            if (Objects.nonNull(groupAuthDto.getGroupSeq())) {
                groupAuthDtoMap.put(groupAuthDto.getGroupSeq(), groupAuthDto);
            }
        }
        return groupAuthDtoMap;
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

    private static String getAccessToken() {
        return jwtTokenProvider.parseTokenCookie(getRequest(),TokenType.ACCESS_TOKEN);
    }
}