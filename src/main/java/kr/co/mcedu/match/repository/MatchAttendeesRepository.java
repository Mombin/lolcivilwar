package kr.co.mcedu.match.repository;

import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchAttendeesRepository extends PagingAndSortingRepository<MatchAttendeesEntity, Long> {
    @Modifying
    @Query("update match_attendees e set e.delYn = true WHERE e.customUserEntity = :entity")
    Integer deleteAllByCustomUserEntity(CustomUserEntity entity);
}