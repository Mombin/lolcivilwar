package kr.co.mcedu.summoner.repository;

import kr.co.mcedu.summoner.entity.SummonerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SummonerRepository extends JpaRepository<SummonerEntity, String> {
    Optional<SummonerEntity> findSummonerEntityByName(String name);
    Optional<SummonerEntity> findSummonerEntityBySearchName(String name);
}