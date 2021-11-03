package kr.co.mcedu.user.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.config.security.AccessTokenField;
import kr.co.mcedu.config.security.JwtTokenProvider;
import kr.co.mcedu.config.security.TokenType;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.model.UserAuthority;
import kr.co.mcedu.user.repository.UserRepository;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.user.service.WebUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.co.mcedu.user.model.UserAuthority.ADMIN;
import static kr.co.mcedu.user.model.UserAuthority.USER;

/**
 * web user 에 관한 Service
 */
@Service
@RequiredArgsConstructor
public class WebUserServiceImpl implements WebUserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final WebUserRepository webUserRepository;
    private final UserRepository userRepository;
    /**
     * userId 를 이용하여 WebUserEntity 검색
     */
    @Override
    public WebUserEntity findWebUserEntityByUserId(String userId) throws AccessDeniedException {
        return webUserRepository.findWebUserEntityByUserId(userId).orElseThrow(() -> new AccessDeniedException("없는 사용자입니다."));
    }

    /**
     * userSeq 를 이용하여 WebUserEntity 검색
     */
    @Override
    public WebUserEntity findWebUserEntity(Long userSeq) throws AccessDeniedException {
        return webUserRepository.findById(userSeq).orElseThrow(() -> new AccessDeniedException("없는 사용자입니다."));
    }

    private List<GrantedAuthority> getAuthorities(String userAuthority) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        switch (UserAuthority.valueOf(userAuthority)) {
            case ADMIN:
                authorities.add(new SimpleGrantedAuthority(ADMIN.getSecurity()));
                authorities.add(new SimpleGrantedAuthority(USER.getSecurity()));
                break;
            case USER:
                authorities.add(new SimpleGrantedAuthority(USER.getSecurity()));
                break;
        }
        return authorities;
    }

    @Override
    public String getAccessToken(String refreshToken) throws ServiceException {
        long userSeq = jwtTokenProvider.getUserSeq(refreshToken);
        WebUserEntity webUserEntity = findWebUserEntity(userSeq);
        if (!refreshToken.equals(webUserEntity.getRefreshToken())) {
            throw new ServiceException("이미 새롭게 로그인된 토큰입니다.");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("sub", webUserEntity.getUserId());
        data.put("userAuth", webUserEntity.getAuthority());
        data.put(AccessTokenField.GROUP_AUTH, userRepository.getGroupAuthList(userSeq));
        data.put("roles", this.getAuthorities(webUserEntity.getAuthority()));
        data.put("userSeq", webUserEntity.getUserSeq());
        String accessToken = jwtTokenProvider.createToken(TokenType.ACCESS_TOKEN, data);
        SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(accessToken));
        return accessToken;
    }
}
