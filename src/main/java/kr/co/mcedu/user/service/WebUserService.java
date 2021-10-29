package kr.co.mcedu.user.service;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.user.entity.WebUserEntity;


public interface WebUserService {
    WebUserEntity findWebUserEntityByUserId(String userId) throws AccessDeniedException;
    WebUserEntity findWebUserEntity(Long userSeq) throws AccessDeniedException;
}