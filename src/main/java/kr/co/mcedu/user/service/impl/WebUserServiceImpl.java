package kr.co.mcedu.user.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.config.security.AccessTokenField;
import kr.co.mcedu.config.security.JwtTokenProvider;
import kr.co.mcedu.config.security.TokenType;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.model.UserAuthority;
import kr.co.mcedu.user.model.UserInfo;
import kr.co.mcedu.user.repository.UserRepository;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<Long, Boolean> needRefreshed = new ConcurrentHashMap<>();

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
        data.put("lolcwTag", webUserEntity.getLolcwTag());
        String accessToken = jwtTokenProvider.createToken(TokenType.ACCESS_TOKEN, data);
        SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(accessToken));
        return accessToken;
    }

    @Override
    public boolean isRefreshedUser(Long userSeq) {
        if (needRefreshed.get(userSeq) == null) {
            return false;
        }
        needRefreshed.remove(userSeq);
        return true;
    }

    @Override
    public void pushRefreshedUser(Long userSeq) {
        needRefreshed.put(userSeq, true);
    }

    /**
     * LOLCW TAG로 사용자 찾기
     * @param lolcwTag
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = true)
    public UserInfo getUserInfoByLolcwTag(final String lolcwTag) throws ServiceException {
        if (StringUtils.isEmpty(lolcwTag)) {
            throw new DataNotExistException("태그를 입력해주세요");
        }
        Optional<WebUserEntity> webUserEntityByLolcwTag = webUserRepository.findWebUserEntityByLolcwTag(lolcwTag);
        WebUserEntity userEntity = webUserEntityByLolcwTag.orElseThrow(() -> new DataNotExistException("존재하지 않는 사용자입니다."));
        return userEntity.getUserInfo();
    }
}
