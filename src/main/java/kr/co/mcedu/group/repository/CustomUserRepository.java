package kr.co.mcedu.group.repository;

import kr.co.mcedu.group.entity.CustomUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUserEntity, Long> {
    List<CustomUserEntity> findAllBySummonerEntityAccountId(String accountId);
    @Query("select cu from custom_user cu left join fetch cu.summonerEntity where cu.seq = :customUserSeq")
    Optional<CustomUserEntity> findByIdFetch(Long customUserSeq);
}