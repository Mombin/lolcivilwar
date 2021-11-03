package kr.co.mcedu.group.repository;

import kr.co.mcedu.group.entity.CustomUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomUserRepository extends JpaRepository<CustomUserEntity, Long> {
    List<CustomUserEntity> findAllBySummonerEntityAccountId(String accountId);
}