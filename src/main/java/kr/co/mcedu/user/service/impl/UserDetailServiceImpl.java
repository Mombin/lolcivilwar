package kr.co.mcedu.user.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.config.security.JwtTokenProvider;
import kr.co.mcedu.config.security.TokenType;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.model.UserRole;
import kr.co.mcedu.user.model.request.JwtRequest;
import kr.co.mcedu.user.model.request.UserRegisterRequest;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.user.service.UserAlarmService;
import kr.co.mcedu.utils.LocalCacheManager;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final WebUserRepository webUserRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final LocalCacheManager localCacheManager;
    private final UserAlarmService userAlarmService;

    @PostConstruct
    @Transactional
    @Deprecated
    public void createLolcwTag() {
        List<WebUserEntity> webUserEntities = webUserRepository.findAllByLolcwTagIsNull();
        webUserEntities.forEach(webUserEntity -> {
            String lolcwTag = StringUtils.getLolcwTag();
            while(true) {
                Optional<WebUserEntity> findByTag = webUserRepository.findWebUserEntityByLolcwTag(lolcwTag);
                if (!findByTag.isPresent()) {
                    break;
                }
                lolcwTag = StringUtils.getLolcwTag();
            }
            webUserEntity.setLolcwTag(lolcwTag);
            webUserRepository.saveAndFlush(webUserEntity);
        });
    }

    @Transactional
    public String login(JwtRequest jwtRequest) throws ServiceException {
        WebUserEntity user = webUserRepository.findWebUserEntityByUserId(jwtRequest.getId())
                                              .orElseThrow(() -> new DataNotExistException("존재하지 않는 사용자입니다."));

        if (!this.checkPassword(jwtRequest.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("비밀번호를 다시 확인해주세요.");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userSeq", user.getUserSeq());
        String token = jwtTokenProvider.createToken(TokenType.REFRESH_TOKEN, data);
        user.setRefreshToken(token);
        webUserRepository.save(user);
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("yache".equals(username)) {
            List<GrantedAuthority> authList = new ArrayList<>();
            authList.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new User("yache", "$2a$10$1AQ2i/GKggzWjnrRPRLWxOeo04Rmp.N2hmtmD7V9haKJM14d8lb36", authList);
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }

    private boolean checkPassword(String pwd, String hashed) {
        return BCrypt.checkpw(pwd, hashed);
    }

    public boolean checkUserByUserId(String userId) {
        Boolean result = localCacheManager.getUserIdCache().getIfPresent(userId);
        if (result == null) {
            result = webUserRepository.findWebUserEntityByUserId(userId).isPresent();
        }
        localCacheManager.getUserIdCache().put(userId, result);
        return result;
    }

    public boolean checkUserByEmail(String email) {
        Boolean result = localCacheManager.getEmailCache().getIfPresent(email);
        if (result == null) {
            result = webUserRepository.findWebUserEntityByEmail(email).isPresent();
        }
        localCacheManager.getEmailCache().put(email, result);
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean registerUser(UserRegisterRequest request) throws ServiceException {
        if (this.checkUserByUserId(request.getId()) || this.checkUserByEmail(request.getEmail())) {
            throw new ServiceException("잘못된 접근입니다.");
        }
        String lolcwTag = StringUtils.getLolcwTag();
        while(true) {
            Optional<WebUserEntity> findByTag = webUserRepository.findWebUserEntityByLolcwTag(lolcwTag);
            if (!findByTag.isPresent()) {
                break;
            }
            lolcwTag = StringUtils.getLolcwTag();
        }

        WebUserEntity entity = new WebUserEntity();
        entity.setUserId(request.getId());
        entity.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        entity.setEmail(request.getEmail());
        entity.setAuthority(UserRole.USER.getCd());
        entity.setLolcwTag(lolcwTag);
        entity = webUserRepository.save(entity);
        if(entity.getUserSeq() == null ) {
            throw new ServiceException("잘못된 접근입니다.");
        }
        localCacheManager.getUserIdCache().invalidate(request.getId());
        localCacheManager.getEmailCache().invalidate(request.getEmail());
        userAlarmService.sendAlarm(entity,
                String.format("회원가입 하신것을 축하드립니다 %s님.<br>[그룹]-[그룹관리] 에서 여러분의 첫번째 팀을 생성해보세요.", request.getId()));
        return true;
    }
}