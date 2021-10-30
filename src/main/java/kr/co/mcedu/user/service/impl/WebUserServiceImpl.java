package kr.co.mcedu.user.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.user.service.WebUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * web user 에 관한 Service
 */
@Service
@RequiredArgsConstructor
public class WebUserServiceImpl implements WebUserService {
    private final WebUserRepository webUserRepository;
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
}
