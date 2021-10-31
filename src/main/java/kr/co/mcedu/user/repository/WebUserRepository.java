package kr.co.mcedu.user.repository;

import kr.co.mcedu.user.entity.WebUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/**
 * WebUserEntity 를 위한 Repository
 */
public interface WebUserRepository extends JpaRepository<WebUserEntity, Long> {
    Optional<WebUserEntity> findWebUserEntityByUserId(String userId);
    Optional<WebUserEntity> findWebUserEntityByEmail(String email);
}
