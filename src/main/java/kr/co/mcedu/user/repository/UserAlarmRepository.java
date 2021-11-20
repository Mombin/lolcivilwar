package kr.co.mcedu.user.repository;

import kr.co.mcedu.user.entity.UserAlarmEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * UserAlarmEntity 를 위한 Repository
 */
public interface UserAlarmRepository extends PagingAndSortingRepository<UserAlarmEntity, Long> {
    @Query("select count(m) from user_alarm m where m.isRead = false and m.webUserEntity = :webUserEntity")
    Long countUnreadAlarm(WebUserEntity webUserEntity);
    @Query("select m from user_alarm m where m.webUserEntity.userSeq = :userSeq and m.isRead = false order by m.createdDate desc")
    List<UserAlarmEntity> getUnreadMessage(Long userSeq);
}