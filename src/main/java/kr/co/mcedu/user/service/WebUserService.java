package kr.co.mcedu.user.service;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.entity.WebUserEntity;

public interface WebUserService {
    WebUserEntity findWebUserEntityByUserId(String userId) throws AccessDeniedException;
    WebUserEntity findWebUserEntity(Long userSeq) throws AccessDeniedException;
    WebUserEntity findWebUserEntityByLolcwTag(String lolcwTag) throws AccessDeniedException;
    String getAccessToken(String refreshToken) throws ServiceException;
    boolean isRefreshedUser(Long userSeq);
    void pushRefreshedUser(Long userSeq);
}