package kr.co.mcedu.group.repository;

import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 그룹권한 Repository
 * @author hil00137@gmail.com
 * @since 2021. 04. 25
 */
@Repository
public interface GroupAuthRepository extends JpaRepository<GroupAuthEntity, Long> {
    @Query("select ga from group_auth ga where ga.webUser.userSeq = :webUserSeq and ga.groupAuth = :groupAuth")
    List<GroupAuthEntity> findAllByWebUserAndGroupAuth(Long webUserSeq, GroupAuthEnum groupAuth);
    List<GroupAuthEntity> findAllByWebUser(WebUserEntity webUserEntity);
    Optional<GroupAuthEntity> findByGroupAndGroupAuth(GroupEntity group, GroupAuthEnum groupAuthEnum);
}