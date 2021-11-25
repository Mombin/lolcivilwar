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
    Optional<WebUserEntity> findWebUserEntityByLolcwTag(String lolcwTag);

    /**
     * 이후 패치에서 삭제
     * @return lolcw 태그 지정안된 유저
     */
    @Deprecated
    List<WebUserEntity> findAllByLolcwTagIsNull();
}
